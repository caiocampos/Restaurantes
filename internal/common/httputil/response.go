package httputil

import "github.com/gin-gonic/gin"

func Error(c *gin.Context, status int, message string) {
	c.AbortWithStatusJSON(status, gin.H{
		"statusCode": status,
		"message":    message,
	})
}
