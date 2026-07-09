package main

import (
	"context"
	"log"
	"time"

	"github.com/gin-gonic/gin"

	"restaurantes/internal/common/jwtutil"
	"restaurantes/internal/common/middleware"
	"restaurantes/internal/config"
	"restaurantes/internal/modules/dish"
	"restaurantes/internal/modules/restaurant"
	"restaurantes/internal/modules/user"
)

func main() {
	cfg := config.Load()

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	client, err := config.ConnectMongo(ctx, cfg.MongoURI)
	if err != nil {
		log.Fatalf("erro ao conectar no MongoDB: %v", err)
	}
	defer func() {
		if err := client.Disconnect(context.Background()); err != nil {
			log.Printf("erro ao desconectar do MongoDB: %v", err)
		}
	}()

	db := client.Database(cfg.DBName)

	tokens := jwtutil.NewManager(cfg.JWTSecret, cfg.JWTExpiresIn)
	authMiddleware := middleware.Auth(tokens)

	restaurantRepo := restaurant.NewRepository(db)
	restaurantService := restaurant.NewService(restaurantRepo)
	restaurantHandler := restaurant.NewHandler(restaurantService)

	dishRepo := dish.NewRepository(db)
	dishService := dish.NewService(dishRepo, restaurantRepo)
	dishHandler := dish.NewHandler(dishService)

	userRepo := user.NewRepository(db)
	userService := user.NewService(userRepo, tokens)
	userHandler := user.NewHandler(userService)

	router := gin.Default()
	api := router.Group("/api")

	restaurant.RegisterRoutes(api, restaurantHandler, authMiddleware)
	dish.RegisterRoutes(api, dishHandler, authMiddleware)
	user.RegisterRoutes(api, userHandler, authMiddleware)

	log.Printf("🍽️  Restaurantes API rodando em http://localhost:%s/api", cfg.Port)
	if err := router.Run(":" + cfg.Port); err != nil {
		log.Fatalf("erro ao iniciar servidor: %v", err)
	}
}
