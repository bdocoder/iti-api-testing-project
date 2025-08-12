package iti.apitesting.requests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import iti.apitesting.base.Logging;

public class Request {
  public HashMap<String, Object> body = new HashMap<>();
  public HashMap<String, Object> headers = new HashMap<>();

  public boolean isLoggingEnabled = true;

  public Request withoutLogging() {
    isLoggingEnabled = false;
    return this;
  }

  public Request withLogging() {
    isLoggingEnabled = true;
    return this;
  }

  public Request withBody(String key, Object value) {
    if (value != null)
      body.put(key, value);
    return this;
  }

  public RequestSpecification prepare() {
    return given().headers(headers).body(body).contentType("application/json");
  }

  private void log(Response response) {
    if (body.size() != 0)
      Logging.info(String.format("Request Body: %s", body));
    Logging.info(String.format("Response Status: %s", response.getStatusCode()));
    Logging.info(String.format("Response Body: %s", response.getBody().asString()));
  }

  public Response post(String url) {
    var response = prepare().when().post(url);

    if (isLoggingEnabled)
      log(response);

    return response;
  }

  public Response get(String url) {
    var response = prepare().when().get(url);

    if (isLoggingEnabled)
      log(response);

    return response;
  }

  public Response patch(String url) {
    var response = prepare().when().patch(url);

    if (isLoggingEnabled)
      log(response);

    return response;
  }

  public Response delete(String url) {
    var response = prepare().when().delete(url);

    if (isLoggingEnabled)
      log(response);

    return response;
  }
}
