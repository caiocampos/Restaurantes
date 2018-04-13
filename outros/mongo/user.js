use tst0campos

db.createCollection('user');
db.getCollection('user').insert({
    "roles": ["user", "admin"],
    "username": "caioc",
    "password": "$2a$10$Mfk8RFBO78xmMTz5YTCF3OT3Fm3AlXYHJpcKTvSCuPgOBaTnVGvYy",
	"nome": "Caio",
	"sobrenome": "de Oliveira Campos",
    "enabled": true
});