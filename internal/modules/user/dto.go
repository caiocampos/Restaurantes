package user

import "restaurantes/internal/common/role"

type CreateRequest struct {
	Username string    `json:"username" binding:"required,min=3"`
	Password string    `json:"password" binding:"required,min=6"`
	Role     role.Role `json:"role" binding:"required,oneof=admin user"`
	Name     string    `json:"name" binding:"required"`
	LastName string    `json:"lastName" binding:"required"`
}

type UpdateRequest struct {
	Username *string    `json:"username" binding:"omitempty,min=3"`
	Role     *role.Role `json:"role" binding:"omitempty,oneof=admin user"`
	Name     *string    `json:"name" binding:"omitempty"`
	LastName *string    `json:"lastName" binding:"omitempty"`
}

type LoginRequest struct {
	Username string `json:"username" binding:"required"`
	Password string `json:"password" binding:"required"`
}

type ChangePasswordRequest struct {
	CurrentPassword string `json:"currentPassword" binding:"required"`
	NewPassword     string `json:"newPassword" binding:"required,min=6"`
}
