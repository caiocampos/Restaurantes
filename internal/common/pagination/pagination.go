package pagination

import (
	"math"
	"strconv"

	"github.com/gin-gonic/gin"
)

type Query struct {
	Page  int64
	Limit int64
	Name  string
}

func Parse(c *gin.Context) Query {
	page, err := strconv.ParseInt(c.DefaultQuery("page", "1"), 10, 64)
	if err != nil || page < 1 {
		page = 1
	}

	limit, err := strconv.ParseInt(c.DefaultQuery("limit", "10"), 10, 64)
	if err != nil || limit < 1 {
		limit = 10
	}

	return Query{Page: page, Limit: limit, Name: c.Query("name")}
}

type Result[T any] struct {
	Data       []T   `json:"data"`
	Total      int64 `json:"total"`
	Page       int64 `json:"page"`
	Limit      int64 `json:"limit"`
	TotalPages int64 `json:"totalPages"`
}

func NewResult[T any](data []T, total, page, limit int64) Result[T] {
	totalPages := int64(math.Ceil(float64(total) / float64(limit)))
	if totalPages < 1 {
		totalPages = 1
	}
	return Result[T]{Data: data, Total: total, Page: page, Limit: limit, TotalPages: totalPages}
}
