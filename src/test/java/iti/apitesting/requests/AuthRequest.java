package iti.apitesting.requests;

import io.restassured.specification.RequestSpecification;
import iti.apitesting.base.Constants;
import iti.apitesting.base.Routes;

public class AuthRequest extends Request {
  @Override
  public RequestSpecification prepare() {
    var response = new Request()
        .withoutLogging()
        .withBody("identity", "admin@localhost.localhost")
        .withBody("password", "password")
        .post(Routes.authWithPassword(Constants.SUPERUSERS_COLLECTION));

    response.then().statusCode(200);
    String token = response.body().jsonPath().getString("token");
    this.headers.put("Authorization", token);

    return super.prepare();
  }
}
