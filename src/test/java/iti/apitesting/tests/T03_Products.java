package iti.apitesting.tests;

import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import iti.apitesting.base.Constants;
import iti.apitesting.base.Helpers;
import iti.apitesting.base.Routes;
import iti.apitesting.data.DataProviders;
import iti.apitesting.data.TestCaseData;
import iti.apitesting.requests.AuthRequest;

@Feature("Products")
public class T03_Products {
  @Test(dataProvider = "invalidProductCreationData", dataProviderClass = DataProviders.class)
  @Story("Cannot create a product with missing/wrong data")
  public void tc11(TestCaseData data) {
    var response = new AuthRequest()
        .withBody("name", data.get("name"))
        .withBody("price", data.get("price"))
        .withBody("stock", data.get("stock"))
        .post(Routes.createRecord(Constants.PRODUCTS_COLLECTION));

    response.then().statusCode(Integer.parseInt(data.get("statusCode").toString()))
        .body("message", containsString(data.get("message").toString()));

    if (data.get("fieldName") != null) {
      response.then().body(String.format("data.%s.message", data.get("fieldName")),
          containsString(data.get("fieldMessage").toString()));
    }
  }

  @Test(priority = 0)
  @Story("Can create a product")
  public void tc12() {
    var response = new AuthRequest()
        .withBody("name", "شاي أخضر")
        .withBody("price", 50)
        .withBody("stock", 20)
        .post(Routes.createRecord(Constants.PRODUCTS_COLLECTION));

    response.then().statusCode(200);
  }

  @Test
  @Story("Cannot get a non-existent product")
  public void tc13() {
    var response = new AuthRequest()
        .get(Routes.viewRecord(Constants.PRODUCTS_COLLECTION, "aaaa"));

    response.then()
        .statusCode(404)
        .body("message", equalTo("The requested resource wasn't found."));
  }

  @Test
  @Story("Cannot edit a non-existent product")
  public void tc14() {
    var response = new AuthRequest()
        .withBody("price", 25)
        .patch(Routes.updateRecord(Constants.PRODUCTS_COLLECTION, "aaaa"));

    response.then().statusCode(404);
    response.then().body("message", equalTo("The requested resource wasn't found."));
  }

  @Test(priority = 1)
  @Story("Cannot edit a product with wrong data")
  public void tc15() {
    var id = Helpers.findProductByName("شاي أخضر").id();
    var response = new AuthRequest()
        .withBody("price", "-12")
        .patch(Routes.updateRecord(Constants.PRODUCTS_COLLECTION, id));

    response.then().statusCode(400);
    response.then().body("message", equalTo("Failed to update record."));
  }

  @Test(priority = 1)
  @Story("Cannot create a product with a duplicate name")
  public void tc16() {
    var response = new AuthRequest()
        .withBody("name", "شاي أخضر")
        .withBody("price", 20)
        .withBody("stock", 15)
        .post(Routes.createRecord(Constants.PRODUCTS_COLLECTION));

    response.then()
        .statusCode(400)
        .body("message", equalTo("Failed to create record."));
  }

  @Test(priority = 2)
  @Story("Can edit a product")
  public void tc17() {
    var id = Helpers.findProductByName("شاي أخضر").id();
    var response = new AuthRequest()
        .withBody("price", 15)
        .patch(Routes.updateRecord(Constants.PRODUCTS_COLLECTION, id));

    response.then().statusCode(200);

    var newProductData = Helpers.findProductByName("شاي أخضر");
    Assert.assertEquals(newProductData.price(), 15);
  }

  @Test(priority = 3)
  @Story("Can delete a product")
  public void tc18() {
    var id = Helpers.findProductByName("شاي أخضر").id();
    var response = new AuthRequest()
        .delete(Routes.deleteRecord(Constants.PRODUCTS_COLLECTION, id));

    response.then().statusCode(204);
  }

  @Test
  @Story("Cannot delete a non-existent product")
  public void tc19() {
    var response = new AuthRequest()
        .delete(Routes.deleteRecord(Constants.PRODUCTS_COLLECTION, "aaaa"));

    response.then()
        .statusCode(404)
        .body("message", equalTo("The requested resource wasn't found."));
  }
}
