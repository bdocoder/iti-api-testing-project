package iti.apitesting.base;

public class Routes {
  public static String authWithPassword(String collectionName) {
    return String.format("%s/collections/%s/auth-with-password", Constants.BASE_URL, collectionName);
  }

  public static String listRecords(String collectionName) {
    return String.format("%s/collections/%s/records", Constants.BASE_URL, collectionName);
  }

  public static String viewRecord(String collectionName, String id) {
    return String.format("%s/collections/%s/records/%s", Constants.BASE_URL, collectionName, id);
  }

  public static String createRecord(String collectionName) {
    return String.format("%s/collections/%s/records", Constants.BASE_URL, collectionName);
  }

  public static String updateRecord(String collectionName, String id) {
    return String.format("%s/collections/%s/records/%s", Constants.BASE_URL, collectionName, id);
  }

  public static String deleteRecord(String collectionName, String id) {
    return String.format("%s/collections/%s/records/%s", Constants.BASE_URL, collectionName, id);
  }
}
