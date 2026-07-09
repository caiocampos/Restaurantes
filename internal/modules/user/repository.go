package user

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
	return &Repository{collection: db.Collection("users")}
}

func (r *Repository) Create(ctx context.Context, u *User) error {
	now := time.Now()
	u.ID = primitive.NewObjectID()
	u.CreatedAt = now
	u.UpdatedAt = now
	_, err := r.collection.InsertOne(ctx, u)
	return err
}

func (r *Repository) FindByUsername(ctx context.Context, username string) (*User, error) {
	var u User
	if err := r.collection.FindOne(ctx, bson.M{"username": username}).Decode(&u); err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *Repository) FindByID(ctx context.Context, id primitive.ObjectID) (*User, error) {
	var u User
	if err := r.collection.FindOne(ctx, bson.M{"_id": id}).Decode(&u); err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *Repository) FindAll(
	ctx context.Context,
	filter bson.M,
	page, limit int64,
) ([]User, int64, error) {
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

	results := make([]User, 0)
	if err := cursor.All(ctx, &results); err != nil {
		return nil, 0, err
	}
	return results, total, nil
}

func (r *Repository) Update(
	ctx context.Context,
	id primitive.ObjectID,
	update bson.M,
) (*User, error) {
	update["updatedAt"] = time.Now()
	if _, err := r.collection.UpdateOne(ctx, bson.M{"_id": id}, bson.M{"$set": update}); err != nil {
		return nil, err
	}
	return r.FindByID(ctx, id)
}

func (r *Repository) SetEnabled(ctx context.Context, id primitive.ObjectID, enabled bool) (*User, error) {
	return r.Update(ctx, id, bson.M{"enabled": enabled})
}
