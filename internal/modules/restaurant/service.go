package restaurant

import (
	"context"
	"errors"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"

	"restaurantes/internal/common/mongoutil"
	"restaurantes/internal/common/pagination"
)

var ErrNotFound = errors.New("restaurante não encontrado")

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) Create(ctx context.Context, req CreateRequest) (*Restaurant, error) {
	restaurant := &Restaurant{Name: req.Name, Phone: req.Phone, Address: req.Address}
	if err := s.repo.Create(ctx, restaurant); err != nil {
		return nil, err
	}
	return restaurant, nil
}

func (s *Service) FindAll(ctx context.Context, query pagination.Query) (pagination.Result[Restaurant], error) {
	filter := mongoutil.NameFilter(query.Name)
	items, total, err := s.repo.FindAll(ctx, filter, query.Page, query.Limit)
	if err != nil {
		return pagination.Result[Restaurant]{}, err
	}
	return pagination.NewResult(items, total, query.Page, query.Limit), nil
}

func (s *Service) FindByID(ctx context.Context, id string) (*Restaurant, error) {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return nil, ErrNotFound
	}

	restaurant, err := s.repo.FindByID(ctx, objID)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrNotFound
		}
		return nil, err
	}
	return restaurant, nil
}

func (s *Service) Update(ctx context.Context, id string, req UpdateRequest) (*Restaurant, error) {
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

	update := bson.M{}
	if req.Name != nil {
		update["name"] = *req.Name
	}
	if req.Phone != nil {
		update["phone"] = *req.Phone
	}
	if req.Address != nil {
		update["address"] = *req.Address
	}
	return s.repo.Update(ctx, objID, update)
}

func (s *Service) Remove(ctx context.Context, id string) error {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return ErrNotFound
	}
	if _, err := s.repo.FindByID(ctx, objID); err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return ErrNotFound
		}
		return err
	}
	return s.repo.Delete(ctx, objID)
}
