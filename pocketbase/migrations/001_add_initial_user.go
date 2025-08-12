package migrations

import (
	"github.com/pocketbase/pocketbase/core"
	m "github.com/pocketbase/pocketbase/migrations"
)

func init() {
	m.Register(func(app core.App) error {
		superusers, err := app.FindCollectionByNameOrId(core.CollectionNameSuperusers)
		if err != nil {
			return err
		}

		admin := core.NewRecord(superusers)
		admin.Set("email", "admin@localhost.localhost")
		admin.Set("password", "password")
		if err := app.Save(admin); err != nil {
			return err
		}

		return nil
	}, nil)
}
