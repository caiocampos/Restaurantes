package dish

import (
	"context"
	"errors"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"

	"restaurantes/internal/common/mongoutil"
	"restaurantes/internal/common/pagination"
	"restaurantes/internal/modules/restaurant"
)

var (
	ErrNotFound           = errors.New("prato não encontrado")
	ErrRestaurantNotFound = errors.New("restaurante informado não existe")
)

type Service struct {
	repo           *Repository
	restaurantRepo *restaurant.Repository
}

func NewService(repo *Repository, restaurantRepo *restaurant.Repository) *Service {
	return &Service{repo: repo, restaurantRepo: restaurantRepo}
}

func (s *Service) ensureRestaurantExists(ctx context.Context, restaurantID string) (primitive.ObjectID, error) {
	objID, err := mongoutil.ParseObjectID(restaurantID)
	if err != nil {
		return primitive.NilObjectID, ErrRestaurantNotFound
	}
	exists, err := s.restaurantRepo.Exists(ctx, objID)
	if err != nil {
		return primitive.NilObjectID, err
	}
	if !exists {
		return primitive.NilObjectID, ErrRestaurantNotFound
	}
	return objID, nil
}

func (s *Service) Create(ctx context.Context, req CreateRequest) (*Dish, error) {
	restaurantObjID, err := s.ensureRestaurantExists(ctx, req.RestaurantID)
	if err != nil {
		return nil, err
	}

	dish := &Dish{Name: req.Name, Price: req.Price, RestaurantID: restaurantObjID}
	if err := s.repo.Create(ctx, dish); err != nil {
		return nil, err
	}
	return dish, nil
}

func (s *Service) FindAll(ctx context.Context, query pagination.Query) (pagination.Result[Dish], error) {
	filter := mongoutil.NameFilter(query.Name)
	items, total, err := s.repo.FindAll(ctx, filter, query.Page, query.Limit)
	if err != nil {
		return pagination.Result[Dish]{}, err
	}
	return pagination.NewResult(items, total, query.Page, query.Limit), nil
}

func (s *Service) FindByID(ctx context.Context, id string) (*Dish, error) {
	objID, err := mongoutil.ParseObjectID(id)
	if err != nil {
		return nil, ErrNotFound
	}

	dish, err := s.repo.FindByID(ctx, objID)
	if err != nil {
		if errors.Is(err, mongo.ErrNoDocuments) {
			return nil, ErrNotFound
		}
		return nil, err
	}
	return dish, nil
}

func (s *Service) Update(ctx context.Context, id string, req UpdateRequest) (*Dish, error) {
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
	if req.Price != nil {
		update["price"] = *req.Price
	}
	if req.RestaurantID != nil {
		restaurantObjID, err := s.ensureRestaurantExists(ctx, *req.RestaurantID)
		if err != nil {
			return nil, err
		}
		update["restaurant_id"] = restaurantObjID
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
