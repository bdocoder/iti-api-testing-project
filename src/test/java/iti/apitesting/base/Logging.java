package iti.apitesting.base;

import org.slf4j.LoggerFactory;

public class Logging {

  public static void error(String message) {
    LoggerFactory.getLogger("").error(message);
  }

  public static void info(String message) {
    LoggerFactory.getLogger("").info(message);
  }
}
