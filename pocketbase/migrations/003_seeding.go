package migrations

import (
	"time"

	"github.com/pocketbase/pocketbase/core"
	m "github.com/pocketbase/pocketbase/migrations"
)

func init() {
	m.Register(func(app core.App) error {
		// --- Customers ---
		customers, err := app.FindCollectionByNameOrId("customers")
		if err != nil {
			return err
		}
		customersData := []map[string]any{
			{"name": "أحمد محمد", "phone": "+201012345678"},
			{"name": "فاطمة علي", "phone": "+201098765432"},
			{"name": "محمد عبد الله", "phone": "+201223344556"},
			{"name": "سارة حسن", "phone": "+201145678901"},
			{"name": "محمود أحمد", "phone": "+201056789012"},
			{"name": "منى إبراهيم", "phone": "+201034567890"},
			{"name": "خالد سمير", "phone": "+201078945612"},
			{"name": "ليلى يوسف", "phone": "+201065432198"},
			{"name": "يوسف طارق", "phone": "+201087654321"},
			{"name": "هالة محمود", "phone": "+201076543219"},
		}

		customerIDs := []string{}
		for _, data := range customersData {
			record := core.NewRecord(customers)
			record.Set("name", data["name"])
			record.Set("phone", data["phone"])
			if err := app.Save(record); err != nil {
				return err
			}
			customerIDs = append(customerIDs, record.Id)
		}

		// --- Products ---
		products, err := app.FindCollectionByNameOrId("products")
		if err != nil {
			return err
		}
		productsData := []map[string]any{
			{"name": "شاي العروسة", "price": 15.0, "stock": 50},
			{"name": "سكر الأسرة 1كجم", "price": 25.0, "stock": 100},
			{"name": "أرز الضحى 1كجم", "price": 22.0, "stock": 80},
			{"name": "زيت كريستال 1لتر", "price": 50.0, "stock": 60},
			{"name": "مكرونة الملكة 400جم", "price": 12.0, "stock": 120},
			{"name": "جبنة بيضاء دومتي 500جم", "price": 70.0, "stock": 40},
			{"name": "لبن جهينة 1لتر", "price": 35.0, "stock": 70},
			{"name": "شيبسي بطاطس 40جم", "price": 10.0, "stock": 200},
			{"name": "بيبسي 1.5لتر", "price": 20.0, "stock": 90},
			{"name": "خبز بلدي", "price": 2.0, "stock": 300},
		}

		productIDs := []string{}
		for _, data := range productsData {
			record := core.NewRecord(products)
			record.Set("name", data["name"])
			record.Set("price", data["price"])
			record.Set("stock", data["stock"])
			if err := app.Save(record); err != nil {
				return err
			}
			productIDs = append(productIDs, record.Id)
		}

		// --- Sales ---
		sales, err := app.FindCollectionByNameOrId("sales")
		if err != nil {
			return err
		}
		salesData := []map[string]any{
			{ // Sale 1
				"timestamp": time.Now().AddDate(0, 0, -3),
				"customer":  customerIDs[0],
				"product":   productIDs[0],
				"quantity":  2,
			},
			{
				"timestamp": time.Now().AddDate(0, 0, -2),
				"customer":  customerIDs[1],
				"product":   productIDs[1],
				"quantity":  1,
			},
			{
				"timestamp": time.Now().AddDate(0, 0, -2),
				"customer":  customerIDs[2],
				"product":   productIDs[4],
				"quantity":  5,
			},
			{
				"timestamp": time.Now().AddDate(0, 0, -1),
				"customer":  customerIDs[3],
				"product":   productIDs[6],
				"quantity":  3,
			},
			{
				"timestamp": time.Now(),
				"customer":  customerIDs[4],
				"product":   productIDs[8],
				"quantity":  2,
			},
		}

		for _, data := range salesData {
			record := core.NewRecord(sales)
			record.Set("timestamp", data["timestamp"])
			record.Set("customer", data["customer"])
			record.Set("product", data["product"])
			record.Set("quantity", data["quantity"])
			if err := app.Save(record); err != nil {
				return err
			}
		}

		// --- Expenses ---
		expenses, err := app.FindCollectionByNameOrId("expenses")
		if err != nil {
			return err
		}
		expensesData := []map[string]any{
			{
				"timestamp":   time.Now().AddDate(0, 0, -7),
				"description": "إيجار المحل",
				"amount":      3000.0,
				"category":    "rent",
			},
			{
				"timestamp":   time.Now().AddDate(0, 0, -5),
				"description": "فاتورة كهرباء",
				"amount":      500.0,
				"category":    "utilities",
			},
			{
				"timestamp":   time.Now().AddDate(0, 0, -4),
				"description": "فاتورة مياه",
				"amount":      150.0,
				"category":    "utilities",
			},
			{
				"timestamp":   time.Now().AddDate(0, 0, -3),
				"description": "شراء أكياس تعبئة",
				"amount":      100.0,
				"category":    "supplies",
			},
			{
				"timestamp":   time.Now().AddDate(0, 0, -2),
				"description": "تصليح ثلاجة",
				"amount":      750.0,
				"category":    "misc",
			},
		}

		for _, data := range expensesData {
			record := core.NewRecord(expenses)
			record.Set("timestamp", data["timestamp"])
			record.Set("description", data["description"])
			record.Set("amount", data["amount"])
			record.Set("category", data["category"])
			if err := app.Save(record); err != nil {
				return err
			}
		}

		return nil
	}, nil)
}
