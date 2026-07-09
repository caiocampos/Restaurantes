package role

type Role string

const (
	Admin Role = "admin"
	User  Role = "user"
)

func IsValid(r Role) bool {
	return r == Admin || r == User
}
