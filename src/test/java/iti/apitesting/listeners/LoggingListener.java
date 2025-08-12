package iti.apitesting.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import iti.apitesting.base.Logging;

public class LoggingListener implements ITestListener {
  @Override
  public void onTestStart(ITestResult result) {
    String message = String.format("%s started", result.getName());
    if (result.getParameters().length == 1)
      message += String.format(" with test data: %s", result.getParameters()[0]);
    Logging.info(message);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    Logging.info(String.format("%s succeeded\n--------", result.getName()));
  }

  @Override
  public void onTestFailure(ITestResult result) {
    Logging.error(String.format("%s failed\n%s\n--------", result.getName(), result.getThrowable()));
  }
}
