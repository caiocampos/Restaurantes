package middleware

import (
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/httputil"
	"restaurantes/internal/common/jwtutil"
)

const (
	ContextUserID   = "userID"
	ContextUsername = "username"
	ContextRole     = "role"
)

func Auth(manager *jwtutil.Manager) gin.HandlerFunc {
	return func(c *gin.Context) {
		header := c.GetHeader("Authorization")
		if header == "" || !strings.HasPrefix(header, "Bearer ") {
			httputil.Error(c, http.StatusUnauthorized, "Token não informado")
			return
		}

		tokenString := strings.TrimPrefix(header, "Bearer ")
		claims, err := manager.Parse(tokenString)
		if err != nil {
			httputil.Error(c, http.StatusUnauthorized, "Token inválido ou expirado")
			return
		}

		c.Set(ContextUserID, claims.UserID)
		c.Set(ContextUsername, claims.Username)
		c.Set(ContextRole, claims.Role)
		c.Next()
	}
}
