package iti.apitesting.base;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import iti.apitesting.requests.AuthRequest;

public class Helpers {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Customer(String id, String name, String phone) {
  };

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Product(String id, String name, double price, double stock) {
  };

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Sale(String id, String customer, String product, double quantity, String timestamp) {
  };

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Expense(String id, String description, String category, double amount, String timestamp) {
  };

  public static <T> List<T> getAllRecords(String collectionName, Class<T> type) {
    var response = new AuthRequest()
        .withoutLogging()
        .get(Routes.listRecords(collectionName));
    response.then().statusCode(200);
    var items = response.body().jsonPath().<T>getList("items", type);
    return items;
  }

  public static Customer findCustomerByPhone(String phone) {
    var customers = getAllRecords(Constants.CUSTOMERS_COLLECTION, Customer.class);
    for (var customer : customers) {
      if (customer.phone().equals(phone))
        return customer;
    }

    return null;
  }

  public static Customer getRandomCustomer() {
    var customers = getAllRecords(Constants.CUSTOMERS_COLLECTION, Customer.class);
    return customers.get((int) Math.floor(Math.random() * customers.size()));
  }

  public static Product findProductByName(String name) {
    var products = getAllRecords(Constants.PRODUCTS_COLLECTION, Product.class);
    for (var product : products) {
      if (product.name().equals(name))
        return product;
    }

    return null;
  }

  public static Sale getLatestSale() {
    var sales = getAllRecords(Constants.SALES_COLLECTION, Sale.class);
    return sales.getLast();
  }

  public static Expense getLatestExpense() {
    var expenses = getAllRecords(Constants.EXPENSES_COLLECTION, Expense.class);
    return expenses.getLast();
  }

  public static String getDate() {
    return java.time.OffsetDateTime.now().toString();
  }
}
