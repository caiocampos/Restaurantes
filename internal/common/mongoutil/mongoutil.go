package mongoutil

import (
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func ParseObjectID(id string) (primitive.ObjectID, error) {
	return primitive.ObjectIDFromHex(id)
}

func NameFilter(name string) bson.M {
	if name == "" {
		return bson.M{}
	}
	return bson.M{"name": bson.M{"$regex": name, "$options": "i"}}
}
