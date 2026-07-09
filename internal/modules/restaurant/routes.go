package restaurant

import (
	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/middleware"
	"restaurantes/internal/common/permissions"
)

func RegisterRoutes(router *gin.RouterGroup, handler *Handler, auth gin.HandlerFunc) {
	group := router.Group("/restaurants", auth)

	group.POST(
		"",
		middleware.RequirePermission(permissions.ModuleRestaurants, permissions.ActionCreate),
		handler.Create,
	)
	group.GET(
		"",
		middleware.RequirePermission(permissions.ModuleRestaurants, permissions.ActionRead),
		handler.FindAll,
	)
	group.GET(
		"/:id",
		middleware.RequirePermission(permissions.ModuleRestaurants, permissions.ActionRead),
		handler.FindOne,
	)
	group.PATCH(
		"/:id",
		middleware.RequirePermission(permissions.ModuleRestaurants, permissions.ActionUpdate),
		handler.Update,
	)
	group.DELETE(
		"/:id",
		middleware.RequirePermission(permissions.ModuleRestaurants, permissions.ActionDelete),
		handler.Remove,
	)
}
