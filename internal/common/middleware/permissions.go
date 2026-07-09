package middleware

import (
	"net/http"

	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/httputil"
	"restaurantes/internal/common/permissions"
	"restaurantes/internal/common/role"
)

func RequirePermission(module permissions.Module, action permissions.Action) gin.HandlerFunc {
	return func(c *gin.Context) {
		value, exists := c.Get(ContextRole)
		if !exists {
			httputil.Error(c, http.StatusForbidden, "Usuário não autenticado")
			return
		}

		r, ok := value.(role.Role)
		if !ok || !permissions.HasPermission(r, module, action) {
			httputil.Error(
				c,
				http.StatusForbidden,
				"Você não tem permissão para executar esta ação",
			)
			return
		}

		c.Next()
	}
}
