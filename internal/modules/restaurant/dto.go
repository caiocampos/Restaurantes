package restaurant

type CreateRequest struct {
	Name    string `json:"name" binding:"required"`
	Phone   string `json:"phone" binding:"required"`
	Address string `json:"address" binding:"required"`
}

type UpdateRequest struct {
	Name    *string `json:"name" binding:"omitempty"`
	Phone   *string `json:"phone" binding:"omitempty"`
	Address *string `json:"address" binding:"omitempty"`
}
