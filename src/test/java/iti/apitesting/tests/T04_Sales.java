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

@Feature("Sales")
public class T04_Sales {
  @Test(dataProvider = "invalidSaleCreationData", dataProviderClass = DataProviders.class)
  @Story("Cannot create a sale with missing/wrong data")
  public void tc20(TestCaseData data) {
    var response = new AuthRequest()
        .withBody("customer", data.get("customerId"))
        .withBody("product", data.get("productId"))
        .withBody("quantity", data.get("quantity"))
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.SALES_COLLECTION));

    response.then()
        .statusCode(400)
        .body("message", equalTo("Failed to create record."))
        .body(String.format("data.%s.message", data.get("fieldName")),
            containsString(data.get("fieldMessage").toString()));
  }

  @Test(priority = 0)
  @Story("Can create a sale")
  public void tc21() {
    var customer = Helpers.findCustomerByPhone("+201012345678");
    var product = Helpers.findProductByName("شاي العروسة");

    var previousStock = product.stock();

    var response = new AuthRequest()
        .withBody("customer", customer.id())
        .withBody("product", product.id())
        .withBody("quantity", 4)
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.SALES_COLLECTION));

    response.then().statusCode(200);

    var currentStock = Helpers.findProductByName("شاي العروسة").stock();

    Assert.assertTrue(previousStock - currentStock == 4, "expected the stock to be less by 4");
  }

  @Test(priority = 1)
  @Story("Can edit a sale")
  public void tc22() {
    var sale = Helpers.getLatestSale();
    var previousStock = Helpers.findProductByName("شاي العروسة").stock();

    var response = new AuthRequest()
        .withBody("quantity", sale.quantity() + 2)
        .patch(Routes.updateRecord(Constants.SALES_COLLECTION, sale.id()));

    var currentStock = Helpers.findProductByName("شاي العروسة").stock();

    response.then().statusCode(200);
    Assert.assertTrue(previousStock - currentStock == 2, "expected the stock to be less by 2");
  }

  @Test(priority = 2)
  @Story("Can delete a sale")
  public void tc23() {
    var sale = Helpers.getLatestSale();
    var previousStock = Helpers.findProductByName("شاي العروسة").stock();
    var response = new AuthRequest()
        .delete(Routes.deleteRecord(Constants.SALES_COLLECTION, sale.id()));

    response.then().statusCode(204);

    var currentStock = Helpers.findProductByName("شاي العروسة").stock();
    Assert.assertTrue(currentStock - previousStock == 6, "expected the stock to be greater by 6");
  }
}
