package user

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"

	"restaurantes/internal/common/role"
)

type User struct {
	ID        primitive.ObjectID `bson:"_id,omitempty" json:"id"`
	Username  string             `bson:"username" json:"username"`
	Password  string             `bson:"password" json:"-"`
	Role      role.Role          `bson:"role" json:"role"`
	Name      string             `bson:"name" json:"name"`
	LastName  string             `bson:"lastName" json:"lastName"`
	Enabled   bool               `bson:"enabled" json:"enabled"`
	CreatedAt time.Time          `bson:"createdAt" json:"createdAt"`
	UpdatedAt time.Time          `bson:"updatedAt" json:"updatedAt"`
}
