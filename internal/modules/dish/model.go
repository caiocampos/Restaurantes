package dish

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

type Dish struct {
	ID           primitive.ObjectID `bson:"_id,omitempty" json:"id"`
	Name         string             `bson:"name" json:"name"`
	Price        float64            `bson:"price" json:"price"`
	RestaurantID primitive.ObjectID `bson:"restaurant_id" json:"restaurant_id"`
	CreatedAt    time.Time          `bson:"createdAt" json:"createdAt"`
	UpdatedAt    time.Time          `bson:"updatedAt" json:"updatedAt"`
}
