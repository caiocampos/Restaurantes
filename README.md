# Restaurantes (Restaurante's)

[![License](https://img.shields.io/github/license/caiocampos/Restaurantes.svg)](LICENSE)

Software com login, gestão de autoridades e CRUD de entidades, usando Go, Gin Gonic e MongoDB

## [Projeto Front-end](https://github.com/caiocampos/restaurantes-app)

## Executando:

Para executar o projeto é necessário o Go instalado e configurado, siga as instruções do site a seguir para configurar:

http://www.golangbr.org/doc/instalacao

Antes de executar o servidor, crie um arquivo .env com configurações para o MongoDB e JWT (utilize o arquivo [.env.example](.env.example) como base).

Após instalar o Go e configurar o arquivo .env, instale as dependências com o seguinte comando:

> go mod tidy

Então utilize o seguinte comando como exemplo para criar o primeiro usuário admin para o sistema:

> go run ./cmd/seed --username admin --password "senha-forte" --name Admin --lastname Master

Com o usuário admin configurado, compile o código, utilize o seguinte comando para isso:

> go build

E depois, para executar:

> ./restaurantes
