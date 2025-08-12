package iti.apitesting.tests;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import iti.apitesting.base.Constants;
import iti.apitesting.base.Routes;
import iti.apitesting.data.DataProviders;
import iti.apitesting.data.TestCaseData;
import iti.apitesting.requests.Request;

@Feature("Login")
public class T01_Login {
    @Test(dataProvider = "loginData", dataProviderClass = DataProviders.class)
    @Story("login api route should return correct status code and body")
    public void tc01(TestCaseData data) {
        var response = new Request()
                .withBody("identity", data.get("identity"))
                .withBody("password", data.get("password"))
                .post(Routes.authWithPassword(Constants.SUPERUSERS_COLLECTION));

        response.then().statusCode(Integer.parseInt(data.get("statusCode").toString()));

        if (response.statusCode() == 200) {
            response.then().body("token", notNullValue());
        } else {
            response.then()
                    .body("message", equalTo(data.get("message")));
        }
    }
}
