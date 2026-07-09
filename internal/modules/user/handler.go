package user

import (
	"errors"
	"net/http"

	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/httputil"
	"restaurantes/internal/common/middleware"
	"restaurantes/internal/common/pagination"
)

type Handler struct {
	service *Service
}

func NewHandler(service *Service) *Handler {
	return &Handler{service: service}
}

func (h *Handler) Login(c *gin.Context) {
	var req LoginRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	result, err := h.service.Login(c.Request.Context(), req)
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, result)
}

func (h *Handler) GetOwnProfile(c *gin.Context) {
	userID, _ := c.Get(middleware.ContextUserID)
	u, err := h.service.FindByID(c.Request.Context(), userID.(string))
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, u)
}

func (h *Handler) ChangeOwnPassword(c *gin.Context) {
	userID, _ := c.Get(middleware.ContextUserID)

	var req ChangePasswordRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	if err := h.service.ChangeOwnPassword(c.Request.Context(), userID.(string), req); err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, gin.H{"success": true})
}

func (h *Handler) Create(c *gin.Context) {
	var req CreateRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	u, err := h.service.Create(c.Request.Context(), req)
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusCreated, u)
}

func (h *Handler) FindAll(c *gin.Context) {
	query := pagination.Parse(c)
	result, err := h.service.FindAll(c.Request.Context(), query)
	if err != nil {
		httputil.Error(c, http.StatusInternalServerError, "Erro ao listar usuários")
		return
	}
	c.JSON(http.StatusOK, result)
}

func (h *Handler) FindOne(c *gin.Context) {
	u, err := h.service.FindByID(c.Request.Context(), c.Param("id"))
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, u)
}

func (h *Handler) Update(c *gin.Context) {
	var req UpdateRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		httputil.Error(c, http.StatusBadRequest, err.Error())
		return
	}

	u, err := h.service.Update(c.Request.Context(), c.Param("id"), req)
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, u)
}

func (h *Handler) Disable(c *gin.Context) {
	u, err := h.service.Disable(c.Request.Context(), c.Param("id"))
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, u)
}

func (h *Handler) Enable(c *gin.Context) {
	u, err := h.service.Enable(c.Request.Context(), c.Param("id"))
	if err != nil {
		h.handleError(c, err)
		return
	}
	c.JSON(http.StatusOK, u)
}

func (h *Handler) handleError(c *gin.Context, err error) {
	switch {
	case errors.Is(err, ErrNotFound):
		httputil.Error(c, http.StatusNotFound, err.Error())
	case errors.Is(err, ErrUsernameTaken):
		httputil.Error(c, http.StatusConflict, err.Error())
	case errors.Is(err, ErrInvalidCreds), errors.Is(err, ErrUserDisabled), errors.Is(err, ErrWrongOldPassword):
		httputil.Error(c, http.StatusUnauthorized, err.Error())
	default:
		httputil.Error(c, http.StatusInternalServerError, "Erro interno")
	}
}
