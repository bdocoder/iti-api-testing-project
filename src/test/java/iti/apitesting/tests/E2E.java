package iti.apitesting.tests;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import iti.apitesting.base.Constants;
import iti.apitesting.base.Helpers;
import iti.apitesting.base.Routes;
import iti.apitesting.requests.AuthRequest;

@Feature("E2E")
public class E2E {

  static String productId;
  static String saleId;

  @Test
  @Story("End-to-end scenario")
  public void e2e() {
    createSaleWithoutProduct();
    createProduct();
    iti.apitesting.base.Logging.info(productId);
    createSale();
    editSale();
    deleteSale();
    deleteProduct();
  }

  @Step("Create a sale with an invalid product")
  public void createSaleWithoutProduct() {
    var response = new AuthRequest()
        .withBody("product", "invalidProductId")
        .withBody("quantity", 2)
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.SALES_COLLECTION));

    response.then()
        .statusCode(400)
        .body("message", equalTo("Failed to create record."));
  }

  @Step("Create a product for the sale")
  public void createProduct() {
    var response = new AuthRequest()
        .withBody("name", "شاي أخضر")
        .withBody("price", 50)
        .withBody("stock", 10)
        .post(Routes.createRecord(Constants.PRODUCTS_COLLECTION));

    response.then().statusCode(200);
    productId = response.body().jsonPath().getString("id");
  }

  @Step("Create the sale")
  public void createSale() {
    var customer = Helpers.getRandomCustomer();
    var response = new AuthRequest()
        .withBody("product", productId)
        .withBody("customer", customer.id())
        .withBody("quantity", 3)
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.SALES_COLLECTION));

    response.then().statusCode(200);
    saleId = response.body().jsonPath().getString("id");
  }

  @Step("Edit sale quantity")
  public void editSale() {
    var response = new AuthRequest()
        .withBody("quantity", 5)
        .patch(Routes.updateRecord(Constants.SALES_COLLECTION, saleId));

    response.then().statusCode(200);
  }

  @Step("Delete the sale")
  public void deleteSale() {
    var response = new AuthRequest()
        .delete(Routes.updateRecord(Constants.SALES_COLLECTION, saleId));

    response.then().statusCode(204);
  }

  @Step("Can delete the product after sale deletion")
  public void deleteProduct() {
    var response = new AuthRequest()
        .delete(Routes.updateRecord(Constants.PRODUCTS_COLLECTION, productId));

    response.then().statusCode(204);
  }
}