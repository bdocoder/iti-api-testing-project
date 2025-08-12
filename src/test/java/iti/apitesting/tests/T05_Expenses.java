package iti.apitesting.tests;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import iti.apitesting.base.Constants;
import iti.apitesting.base.Helpers;
import iti.apitesting.base.Routes;
import iti.apitesting.requests.AuthRequest;

@Feature("Expenses")
public class T05_Expenses {
  @Test
  @Story("Cannot create an expense with wrong category")
  public void tc24() {
    var response = new AuthRequest()
        .withBody("description", "شراء أكياس تعبئة")
        .withBody("amount", 100)
        .withBody("category", "otherCategory")
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.EXPENSES_COLLECTION));

    response.then()
        .statusCode(400)
        .body("message", equalTo("Failed to create record."))
        .body("data.category.message", equalTo("Invalid value otherCategory."));
  }

  @Test(priority = 0)
  @Story("Can create an expense")
  public void tc25() {
    var response = new AuthRequest()
        .withBody("description", "شراء أكياس تعبئة")
        .withBody("amount", 100)
        .withBody("category", "supplies")
        .withBody("timestamp", Helpers.getDate())
        .post(Routes.createRecord(Constants.EXPENSES_COLLECTION));

    response.then().statusCode(200);
  }

  @Test(priority = 1)
  @Story("Can edit an expense")
  public void tc26() {
    var expense = Helpers.getLatestExpense();
    var response = new AuthRequest()
        .withBody("amount", 150)
        .patch(Routes.updateRecord(Constants.EXPENSES_COLLECTION, expense.id()));

    response.then().statusCode(200);
  }

  @Test(priority = 2)
  @Story("Can delete an expense")
  public void tc27() {
    var expense = Helpers.getLatestExpense();
    var response = new AuthRequest()
        .delete(Routes.updateRecord(Constants.EXPENSES_COLLECTION, expense.id()));

    response.then().statusCode(204);
  }
}
