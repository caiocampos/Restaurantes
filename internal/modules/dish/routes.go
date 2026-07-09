package dish

import (
	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/middleware"
	"restaurantes/internal/common/permissions"
)

func RegisterRoutes(router *gin.RouterGroup, handler *Handler, auth gin.HandlerFunc) {
	group := router.Group("/dishes", auth)

	group.POST(
		"",
		middleware.RequirePermission(permissions.ModuleDishes, permissions.ActionCreate),
		handler.Create,
	)
	group.GET(
		"",
		middleware.RequirePermission(permissions.ModuleDishes, permissions.ActionRead),
		handler.FindAll,
	)
	group.GET(
		"/:id",
		middleware.RequirePermission(permissions.ModuleDishes, permissions.ActionRead),
		handler.FindOne,
	)
	group.PATCH(
		"/:id",
		middleware.RequirePermission(permissions.ModuleDishes, permissions.ActionUpdate),
		handler.Update,
	)
	group.DELETE(
		"/:id",
		middleware.RequirePermission(permissions.ModuleDishes, permissions.ActionDelete),
		handler.Remove,
	)
}
