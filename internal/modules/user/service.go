package user

import (
	"context"
	"errors"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"

	"restaurantes/internal/common/jwtutil"
	"restaurantes/internal/common/mongoutil"
	"restaurantes/internal/common/pagination"
	"restaurantes/internal/common/role"
	"restaurantes/internal/common/security"
)

var (
	ErrNotFound         = errors.New("usuário não encontrado")
	ErrUsernameTaken    = errors.New("nome de usuário já está em uso")
	ErrInvalidCreds     = errors.New("usuário ou senha inválidos")
	ErrUserDisabled     = errors.New("usuário desabilitado")
	ErrWrongOldPassword = errors.New("senha atual incorreta")
)

type Service struct {
	repo   *Repository
	tokens *jwtutil.Manager
}

func NewService(repo *Repository, tokens *jwtutil.Manager) *Service {
	return &Service{repo: repo, tokens: tokens}
}

func (s *Service) Create(ctx context.Context, req CreateRequest) (*User, error) {
	existing, err := s.repo.FindByUsername(ctx, req.Username)
	if err != nil && !errors.Is(err, mongo.ErrNoDocuments) {
		return nil, err
	}
	if existing != nil {
		return nil, ErrUsernameTaken
	}

	hashed, err := security.Hash(req.Password)
	if err != nil {
		return nil, err
	}

	u := &User{
		Username: req.Username,
		Password: hashed,
		Role:     req.Role,
		Name:     req.Name,
		LastName: req.LastName,
		Enabled:  true,
	}
	if err := s.repo.Create(ctx, u); err != nil {
		return nil, err
	}
	return u, nil
}

func (s *Service) FindAll(ctx context.Context, query pagination.Query) (pagination.Result[User], error) {
	filter := mongoutil.NameFilter(query.Name)
	items, total, err := s.repo.FindAll(ctx, filter, query.Page, query.Limit)
	if err != nil {
		return pagination.Result[User]{}, err
	}
	return pagination.NewResult(items, total, query.Page, query.Limit), nil
}

func (s *Service) FindByID(ctx context.Context, id string) (*User, error) {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return nil, ErrNotFound
	}

	u, err := s.repo.FindByID(ctx, objID)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrNotFound
		}
		return nil, err
	}
	return u, nil
}

func (s *Service) Update(ctx context.Context, id string, req UpdateRequest) (*User, error) {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return nil, ErrNotFound
	}

	current, err := s.repo.FindByID(ctx, objID)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrNotFound
		}
		return nil, err
	}

	update := bson.M{}
	if req.Username != nil && *req.Username != current.Username {
		existing, err := s.repo.FindByUsername(ctx, *req.Username)
		if err != nil && !errors.Is(err, mongo.ErrNoDocuments) {
			return nil, err
		}
		if existing != nil {
			return nil, ErrUsernameTaken
		}
		update["username"] = *req.Username
	}
	if req.Role != nil {
		update["role"] = *req.Role
	}
	if req.Name != nil {
		update["name"] = *req.Name
	}
	if req.LastName != nil {
		update["lastName"] = *req.LastName
	}

	return s.repo.Update(ctx, objID, update)
}

func (s *Service) Disable(ctx context.Context, id string) (*User, error) {
	return s.setEnabled(ctx, id, false)
}

func (s *Service) Enable(ctx context.Context, id string) (*User, error) {
	return s.setEnabled(ctx, id, true)
}

func (s *Service) setEnabled(ctx context.Context, id string, enabled bool) (*User, error) {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return nil, ErrNotFound
	}
	if _, err := s.repo.FindByID(ctx, objID); err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrNotFound
		}
		return nil, err
	}
	return s.repo.SetEnabled(ctx, objID, enabled)
}

func (s *Service) ChangeOwnPassword(ctx context.Context, userID string, req ChangePasswordRequest) error {
	objID, err := mongoutil.ParseObjectID(userID)
	if err != nil {
		return ErrNotFound
	}

	u, err := s.repo.FindByID(ctx, objID)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return ErrNotFound
		}
		return err
	}

	if !security.Compare(u.Password, req.CurrentPassword) {
		return ErrWrongOldPassword
	}

	hashed, err := security.Hash(req.NewPassword)
	if err != nil {
		return err
	}

	_, err = s.repo.Update(ctx, objID, bson.M{"password": hashed})
	return err
}

type LoginResult struct {
	AccessToken string       `json:"accessToken"`
	User        LoginProfile `json:"user"`
}

type LoginProfile struct {
	Username string    `json:"username"`
	Role     role.Role `json:"role"`
	Name     string    `json:"name"`
	LastName string    `json:"lastName"`
}

func (s *Service) Login(ctx context.Context, req LoginRequest) (*LoginResult, error) {
	u, err := s.repo.FindByUsername(ctx, req.Username)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrInvalidCreds
		}
		return nil, err
	}
	if !u.Enabled {
		return nil, ErrUserDisabled
	}
	if !security.Compare(u.Password, req.Password) {
		return nil, ErrInvalidCreds
	}

	token, err := s.tokens.Generate(u.ID.Hex(), u.Username, u.Role)
	if err != nil {
		return nil, err
	}

	return &LoginResult{
		AccessToken: token,
		User: LoginProfile{
			Username: u.Username,
			Role:     u.Role,
			Name:     u.Name,
			LastName: u.LastName,
		},
	}, nil
}
