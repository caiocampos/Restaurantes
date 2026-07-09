package security

import "golang.org/x/crypto/bcrypt"

const cost = bcrypt.DefaultCost

func Hash(password string) (string, error) {
	hashed, err := bcrypt.GenerateFromPassword([]byte(password), cost)
	if err != nil {
		return "", err
	}
	return string(hashed), nil
}

func Compare(hash, password string) bool {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}
