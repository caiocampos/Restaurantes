package user

import (
	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/middleware"
	"restaurantes/internal/common/permissions"
)

func RegisterRoutes(router *gin.RouterGroup, handler *Handler, auth gin.HandlerFunc) {
	group := router.Group("/users")

	group.POST("/login", handler.Login)

	authenticated := group.Group("", auth)

	authenticated.GET("/me", handler.GetOwnProfile)
	authenticated.PATCH("/me/password", handler.ChangeOwnPassword)

	authenticated.POST(
		"",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionCreate),
		handler.Create,
	)
	authenticated.GET(
		"",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionRead),
		handler.FindAll,
	)
	authenticated.GET(
		"/:id",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionRead),
		handler.FindOne,
	)
	authenticated.PATCH(
		"/:id",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionUpdate),
		handler.Update,
	)
	authenticated.DELETE(
		"/:id",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionDelete),
		handler.Disable,
	)
	authenticated.PATCH(
		"/:id/enable",
		middleware.RequirePermission(permissions.ModuleUsers, permissions.ActionDelete),
		handler.Enable,
	)
}
