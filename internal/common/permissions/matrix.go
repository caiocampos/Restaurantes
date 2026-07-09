package permissions

import "restaurantes/internal/common/role"

type Module string

type Action string

const (
	ModuleDishes      Module = "dishes"
	ModuleRestaurants Module = "restaurants"
	ModuleUsers       Module = "users"

	ActionCreate Action = "create"
	ActionRead   Action = "read"
	ActionUpdate Action = "update"
	ActionDelete Action = "delete"
)

type actionSet map[Action]bool

var matrix = map[role.Role]map[Module]actionSet{
	role.Admin: {
		ModuleDishes:      {ActionCreate: true, ActionRead: true, ActionUpdate: true, ActionDelete: true},
		ModuleRestaurants: {ActionCreate: true, ActionRead: true, ActionUpdate: true, ActionDelete: true},
		ModuleUsers:       {ActionCreate: true, ActionRead: true, ActionUpdate: true, ActionDelete: true},
	},
	role.User: {
		ModuleDishes:      {ActionCreate: true, ActionRead: true, ActionUpdate: true, ActionDelete: true},
		ModuleRestaurants: {ActionCreate: false, ActionRead: true, ActionUpdate: false, ActionDelete: false},
		ModuleUsers:       {ActionCreate: false, ActionRead: true, ActionUpdate: false, ActionDelete: false},
	},
}

func HasPermission(r role.Role, module Module, action Action) bool {
	moduleActions, ok := matrix[r]
	if !ok {
		return false
	}
	actions, ok := moduleActions[module]
	if !ok {
		return false
	}
	return actions[action]
}
