package config

import (
	"log"
	"os"
	"time"

	"github.com/joho/godotenv"
)

type Config struct {
	Port         string
	MongoURI     string
	DBName       string
	JWTSecret    string
	JWTExpiresIn time.Duration
}

func Load() Config {
	if err := godotenv.Load(); err != nil {
		log.Println("Nenhum arquivo .env encontrado, usando variáveis de ambiente do sistema")
	}

	mongoURI := getMultiEnv([]string{"MONGO_URI_RESTAURANTS", "MONGO_URI"})
	if mongoURI == "" {
		log.Fatal("Variável de ambiente MONGO_URI é obrigatória")
	}

	jwtSecret := getMultiEnv([]string{"JWT_SECRET_RESTAURANTS", "JWT_SECRET"})
	if jwtSecret == "" {
		log.Fatal("Variável de ambiente JWT_SECRET é obrigatória")
	}

	expiresRaw := getMultiEnvWithFallback([]string{"JWT_EXPIRES_IN_RESTAURANTS", "JWT_EXPIRES_IN"}, "24h")
	expires, err := time.ParseDuration(expiresRaw)
	if err != nil {
		log.Fatalf(
			"JWT_EXPIRES_IN inválido: %v",
			err,
		)
	}

	return Config{
		Port:         getEnvWithFallback("PORT", "3000"),
		MongoURI:     mongoURI,
		DBName:       getEnvWithFallback("DB_NAME", "restaurantes"),
		JWTSecret:    jwtSecret,
		JWTExpiresIn: expires,
	}
}

func getEnvWithFallback(key string, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

func getMultiEnvWithFallback(keys []string, fallback string) string {
	for _, key := range keys {
		if value := os.Getenv(key); value != "" {
			return value
		}
	}
	return fallback
}

func getMultiEnv(keys []string) string {
	return getMultiEnvWithFallback(keys, "")
}
