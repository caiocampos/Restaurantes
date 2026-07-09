package restaurant

import (
	"context"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

type Repository struct {
	collection *mongo.Collection
}

func NewRepository(db *mongo.Database) *Repository {
	return &Repository{collection: db.Collection("restaurants")}
}

func (r *Repository) Create(ctx context.Context, restaurant *Restaurant) error {
	now := time.Now()
	restaurant.ID = primitive.NewObjectID()
	restaurant.CreatedAt = now
	restaurant.UpdatedAt = now
	_, err := r.collection.InsertOne(ctx, restaurant)
	return err
}

func (r *Repository) FindByID(ctx context.Context, id primitive.ObjectID) (*Restaurant, error) {
	var result Restaurant
	if err := r.collection.FindOne(ctx, bson.M{"_id": id}).Decode(&result); err != nil {
		return nil, err
	}
	return &result, nil
}

func (r *Repository) Exists(ctx context.Context, id primitive.ObjectID) (bool, error) {
	count, err := r.collection.CountDocuments(ctx, bson.M{"_id": id})
	if err != nil {
		return false, err
	}
	return count > 0, nil
}

func (r *Repository) FindAll(
	ctx context.Context,
	filter bson.M,
	page, limit int64,
) ([]Restaurant, int64, error) {
	total, err := r.collection.CountDocuments(ctx, filter)
	if err != nil {
		return nil, 0, err
	}

	opts := options.Find().
		SetSkip((page - 1) * limit).
		SetLimit(limit).
		SetSort(bson.M{"createdAt": -1})

	cursor, err := r.collection.Find(ctx, filter, opts)
	if err != nil {
		return nil, 0, err
	}
	defer cursor.Close(ctx)

	results := make([]Restaurant, 0)
	if err := cursor.All(ctx, &results); err != nil {
		return nil, 0, err
	}
	return results, total, nil
}

func (r *Repository) Update(
	ctx context.Context,
	id primitive.ObjectID,
	update bson.M,
) (*Restaurant, error) {
	update["updatedAt"] = time.Now()
	if _, err := r.collection.UpdateOne(ctx, bson.M{"_id": id}, bson.M{"$set": update}); err != nil {
		return nil, err
	}
	return r.FindByID(ctx, id)
}

func (r *Repository) Delete(ctx context.Context, id primitive.ObjectID) error {
	_, err := r.collection.DeleteOne(ctx, bson.M{"_id": id})
	return err
}
