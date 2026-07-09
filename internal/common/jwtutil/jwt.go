package jwtutil

import (
	"errors"
	"time"

	"github.com/golang-jwt/jwt/v5"

	"restaurantes/internal/common/role"
)

type Claims struct {
	UserID   string    `json:"sub"`
	Username string    `json:"username"`
	Role     role.Role `json:"role"`
	jwt.RegisteredClaims
}

type Manager struct {
	secret  []byte
	expires time.Duration
}

func NewManager(secret string, expires time.Duration) *Manager {
	return &Manager{secret: []byte(secret), expires: expires}
}

func (m *Manager) Generate(userID, username string, r role.Role) (string, error) {
	now := time.Now()
	claims := Claims{
		UserID:   userID,
		Username: username,
		Role:     r,
		RegisteredClaims: jwt.RegisteredClaims{
			IssuedAt:  jwt.NewNumericDate(now),
			ExpiresAt: jwt.NewNumericDate(now.Add(m.expires)),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString(m.secret)
}

func (m *Manager) Parse(tokenString string) (*Claims, error) {
	claims := &Claims{}
	token, err := jwt.ParseWithClaims(tokenString, claims, func(t *jwt.Token) (interface{}, error) {
		if _, ok := t.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("método de assinatura inesperado")
		}
		return m.secret, nil
	})
	if err != nil {
		return nil, err
	}
	if !token.Valid {
		return nil, errors.New("token inválido")
	}
	return claims, nil
}
