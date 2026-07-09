package dish

import (
	"errors"
	"net/http"

	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/httputil"
	"restaurantes/internal/common/pagination"
)

type Handler struct {
	service *Service
}

func NewHandler(service *Service) *Handler {
	return &Handler{service: service}
}

func (h *Handler) Create(c *gin.Context) {
	var req CreateRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	dish, err := h.service.Create(c.Request.Context(), req)
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusCreated, dish)
}

func (h *Handler) FindAll(c *gin.Context) {
	query := pagination.Parse(c)
	result, err := h.service.FindAll(c.Request.Context(), query)
	if err != nil {
		httputil.Error(c, http.StatusInternalServerError, "Erro ao listar pratos")
		return
	}
	c.JSON(http.StatusOK, result)
}

func (h *Handler) FindOne(c *gin.Context) {
	dish, err := h.service.FindByID(c.Request.Context(), c.Param("id"))
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, dish)
}

func (h *Handler) Update(c *gin.Context) {
	var req UpdateRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	dish, err := h.service.Update(c.Request.Context(), c.Param("id"), req)
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, dish)
}

func (h *Handler) Remove(c *gin.Context) {
	if err := h.service.Remove(c.Request.Context(), c.Param("id")); err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, gin.H{"deleted": true})
}

func (h *Handler) handleError(c *gin.Context, err error) {
	switch {
	case errors.Is(err, ErrNotFound), errors.Is(err, ErrRestaurantNotFound):
		httputil.Error(c, http.StatusNotFound, err.Error())
	default:
		httputil.Error(c, http.StatusInternalServerError, "Erro interno")
	}
}
