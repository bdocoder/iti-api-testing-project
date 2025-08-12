package main

import (
	"errors"
	"fmt"
	"log"

	"github.com/pocketbase/pocketbase"
	"github.com/pocketbase/pocketbase/core"
	"github.com/pocketbase/pocketbase/plugins/migratecmd"

	_ "pocketbase/migrations"
)

func main() {
	app := pocketbase.New()

	migratecmd.MustRegister(app, app.RootCmd, migratecmd.Config{})

	app.OnRecordCreate("sales").BindFunc(func(e *core.RecordEvent) error {
		// Get the incoming sale data
		productID := e.Record.GetString("product")
		quantityRequested := e.Record.GetInt("quantity")

		// Find the related product
		product, err := e.App.FindRecordById("products", productID)
		if err != nil {
			return errors.New("المنتج غير موجود")
		}

		// Check stock availability
		currentStock := product.GetInt("stock")
		if quantityRequested <= 0 {
			return errors.New("الكمية المطلوبة يجب أن تكون أكبر من صفر")
		}
		if currentStock < quantityRequested {
			return errors.New("الكمية المتوفرة غير كافية")
		}

		// ✅ Stock is enough → deduct it
		product.Set("stock", currentStock-quantityRequested)
		if err := e.App.Save(product); err != nil {
			log.Println("خطأ أثناء تحديث الكمية:", err)
			return errors.New("تعذر تحديث كمية المنتج")
		}

		return e.Next()
	})

	app.OnRecordUpdate("sales").BindFunc(func(e *core.RecordEvent) error {
		oldSale, err := e.App.FindRecordById("sales", e.Record.Id)
		if err != nil {
			return err
		}

		oldQuantity := oldSale.GetFloat("quantity")
		newQuantity := e.Record.GetFloat("quantity")

		product, err := e.App.FindRecordById("products", e.Record.GetString("product"))
		if err != nil {
			return err
		}

		oldStock := product.GetFloat("stock")
		updatedStock := oldStock + oldQuantity - newQuantity
		if updatedStock < 0 {
			return fmt.Errorf("insufficient stock")
		}

		product.Set("stock", oldStock+oldQuantity-newQuantity)

		if err := e.App.Save(product); err != nil {
			return err
		}

		return e.Next()
	})

	app.OnRecordDelete("sales").BindFunc(func(e *core.RecordEvent) error {

		// Get product ID and quantity from the deleted sale
		productID := e.Record.GetString("product")
		quantitySold := e.Record.GetInt("quantity")

		// Find the related product
		product, err := e.App.FindRecordById("products", productID)
		if err != nil {
			return errors.New("المنتج غير موجود لاستعادة الكمية")
		}

		// Restore stock
		currentStock := product.GetInt("stock")
		product.Set("stock", currentStock+quantitySold)

		if err := e.App.Save(product); err != nil {
			log.Println("خطأ أثناء استعادة الكمية:", err)
			return errors.New("تعذر استعادة كمية المنتج")
		}

		return e.Next()
	})

	if err := app.Start(); err != nil {
		panic(err)
	}
}
