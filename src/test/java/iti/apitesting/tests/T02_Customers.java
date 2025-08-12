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

@Feature("Customers")
public class T02_Customers {
  @Test(dataProvider = "invalidCustomerCreationData", dataProviderClass = DataProviders.class)
  @Story("Cannot create a customer with missing/wrong data")
  public void tc02(TestCaseData data) {
    var response = new AuthRequest()
        .withBody("name", data.get("name"))
        .withBody("phone", data.get("phone"))
        .post(Routes.createRecord(Constants.CUSTOMERS_COLLECTION));

    response.then().statusCode(Integer.parseInt(data.get("statusCode").toString()))
        .body("message", equalTo(data.get("message")));

    if (data.get("fieldName") != null) {
      response.then().body(String.format("data.%s.message", data.get("fieldName")), equalTo(data.get("fieldMessage")));
    }
  }

  @Test(priority = 0)
  @Story("Can create a customer")
  public void tc03() {
    var response = new AuthRequest()
        .withBody("name", "محمد علي")
        .withBody("phone", "+201234567890")
        .post(Routes.createRecord(Constants.CUSTOMERS_COLLECTION));

    response.then().statusCode(200);
  }

  @Test
  @Story("Cannot get a non-existent customer")
  public void tc04() {
    var response = new AuthRequest()
        .get(Routes.viewRecord(Constants.CUSTOMERS_COLLECTION, "aaaa"));

    response.then()
        .statusCode(404)
        .body("message", equalTo("The requested resource wasn't found."));
  }

  @Test
  @Story("Cannot edit a non-existent customer")
  public void tc05() {
    var response = new AuthRequest()
        .withBody("name", "محمود علي")
        .withBody("phone", "+201234567891")
        .patch(Routes.updateRecord(Constants.CUSTOMERS_COLLECTION, "aaaa"));

    response.then().statusCode(404);
    response.then().body("message", equalTo("The requested resource wasn't found."));
  }

  @Test(priority = 1)
  @Story("Cannot edit a customer with wrong data")
  public void tc06() {
    var id = Helpers.findCustomerByPhone("+201234567890").id();
    var response = new AuthRequest()
        .withBody("phone", "+2012")
        .patch(Routes.updateRecord(Constants.CUSTOMERS_COLLECTION, id));

    response.then().statusCode(400);
    response.then().body("message", equalTo("Failed to update record."));
  }

  @Test(priority = 1)
  @Story("Cannot create a customer with a duplicate phone number")
  public void tc07() {
    var existing = Helpers.findCustomerByPhone("+201234567890");
    var response = new AuthRequest()
        .withBody("name", "...")
        .withBody("phone", existing.phone())
        .post(Routes.createRecord(Constants.CUSTOMERS_COLLECTION));

    response.then()
        .statusCode(400)
        .body("message", equalTo("Failed to create record."));
  }

  @Test(priority = 2)
  @Story("Can edit a customer")
  public void tc08() {
    var id = Helpers.findCustomerByPhone("+201234567890").id();
    var response = new AuthRequest()
        .withBody("name", "محمود علي")
        .withBody("phone", "+201234567891")
        .patch(Routes.updateRecord(Constants.CUSTOMERS_COLLECTION, id));

    response.then().statusCode(200);

    var newCustomerData = Helpers.findCustomerByPhone("+201234567891");
    Assert.assertEquals(newCustomerData.name(), "محمود علي");
  }

  @Test(priority = 3)
  @Story("Can delete a customer")
  public void tc09() {
    var id = Helpers.findCustomerByPhone("+201234567891").id();
    var response = new AuthRequest()
        .delete(Routes.deleteRecord(Constants.CUSTOMERS_COLLECTION, id));

    response.then().statusCode(204);
  }

  @Test
  @Story("Cannot delete a non-existent customer")
  public void tc10() {
    var response = new AuthRequest()
        .delete(Routes.deleteRecord(Constants.CUSTOMERS_COLLECTION, "aaaa"));

    response.then()
        .statusCode(404)
        .body("message", equalTo("The requested resource wasn't found."));
  }
}
