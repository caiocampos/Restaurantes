package restaurant

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

type Restaurant struct {
	ID        primitive.ObjectID `bson:"_id,omitempty" json:"id"`
	Name      string             `bson:"name" json:"name"`
	Phone     string             `bson:"phone" json:"phone"`
	Address   string             `bson:"address" json:"address"`
	CreatedAt time.Time          `bson:"createdAt" json:"createdAt"`
	UpdatedAt time.Time          `bson:"updatedAt" json:"updatedAt"`
}
