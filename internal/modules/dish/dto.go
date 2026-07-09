package dish

type CreateRequest struct {
	Name         string  `json:"name" binding:"required"`
	Price        float64 `json:"price" binding:"required,gt=0"`
	RestaurantID string  `json:"restaurant_id" binding:"required"`
}

type UpdateRequest struct {
	Name         *string  `json:"name" binding:"omitempty"`
	Price        *float64 `json:"price" binding:"omitempty,gt=0"`
	RestaurantID *string  `json:"restaurant_id" binding:"omitempty"`
}
