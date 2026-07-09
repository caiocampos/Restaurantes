package main

import (
	"context"
	"flag"
	"log"
	"time"

	"restaurantes/internal/common/role"
	"restaurantes/internal/common/security"
	"restaurantes/internal/config"
	"restaurantes/internal/modules/user"
)

func main() {
	username := flag.String("username", "admin", "username do admin inicial")
	password := flag.String("password", "", "senha do admin inicial (obrigatório)")
	name := flag.String("name", "Admin", "nome")
	lastName := flag.String("lastname", "Master", "sobrenome")
	flag.Parse()

	if *password == "" {
		log.Fatal("informe --password para criar o admin inicial")
	}

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
	repo := user.NewRepository(db)

	existing, err := repo.FindByUsername(ctx, *username)
	if err == nil && existing != nil {
		log.Printf("usuário %q já existe, nada a fazer", *username)
		return
	}

	hashed, err := security.Hash(*password)
	if err != nil {
		log.Fatalf("erro ao gerar hash da senha: %v", err)
	}

	admin := &user.User{
		Username: *username,
		Password: hashed,
		Role:     role.Admin,
		Name:     *name,
		LastName: *lastName,
		Enabled:  true,
	}
	if err := repo.Create(ctx, admin); err != nil {
		log.Fatalf("erro ao criar admin: %v", err)
	}

	log.Printf("usuário admin %q criado com sucesso", *username)
}
