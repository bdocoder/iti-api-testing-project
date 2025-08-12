package migrations

import (
	"github.com/pocketbase/pocketbase/core"
	m "github.com/pocketbase/pocketbase/migrations"
)

func init() {
	m.Register(func(app core.App) error {
		minProductPrice := float64(0.5)
		minProductStock := float64(0)
		minQuantityField := float64(0.5)
		minExpenseAmount := float64(0.5)

		createdField := core.AutodateField{
			Name:     "created",
			OnCreate: true,
		}
		updatedField := core.AutodateField{
			Name:     "updated",
			OnCreate: true,
			OnUpdate: true,
		}
		timestampField := core.DateField{
			Name:     "timestamp",
			Required: true,
		}
		nameField := core.TextField{
			Name:     "name",
			Required: true,
		}
		phoneField := core.TextField{
			Name:     "phone",
			Required: true,
			Pattern:  "^(\\+2|2)?0\\d{10}$",
		}
		priceField := core.NumberField{
			Name:     "price",
			Required: true,
			Min:      &minProductPrice,
		}
		stockField := core.NumberField{
			Name: "stock",
			Min:  &minProductStock,
		}
		quantityField := core.NumberField{
			Name:     "quantity",
			Required: true,
			Min:      &minQuantityField,
		}

		descriptionField := core.TextField{
			Name:     "description",
			Required: true,
		}
		amountField := core.NumberField{
			Name:     "amount",
			Required: true,
			Min:      &minExpenseAmount,
		}
		categoryField := core.SelectField{
			Name:     "category",
			Required: true,
			Values:   []string{"rent", "utilities", "supplies", "misc"},
		}

		customers := core.NewBaseCollection("customers")
		customers.Fields.Add(
			&createdField,
			&updatedField,
			&nameField,
			&phoneField,
		)
		customers.AddIndex("idx_customers_phone", true, "phone", "")
		if err := app.Save(customers); err != nil {
			return err
		}
		customerField := core.RelationField{
			Name:         "customer",
			CollectionId: customers.Id,
			Required:     true,
		}

		products := core.NewBaseCollection("products")
		products.Fields.Add(
			&createdField,
			&updatedField,
			&nameField,
			&priceField,
			&stockField,
		)
		products.AddIndex("idx_products_name", true, "name", "")
		if err := app.Save(products); err != nil {
			return err
		}
		productField := core.RelationField{
			Name:         "product",
			CollectionId: products.Id,
			Required:     true,
		}

		sales := core.NewBaseCollection("sales")
		sales.Fields.Add(
			&createdField,
			&updatedField,
			&timestampField,
			&customerField,
			&productField,
			&quantityField,
		)
		if err := app.Save(sales); err != nil {
			return err
		}

		expenses := core.NewBaseCollection("expenses")
		expenses.Fields.Add(
			&createdField,
			&updatedField,
			&timestampField,
			&descriptionField,
			&amountField,
			&categoryField,
		)
		if err := app.Save(expenses); err != nil {
			return err
		}

		return nil
	}, nil)
}
