package iti.apitesting.data;

import java.util.HashMap;

public class TestCaseData extends HashMap<String, Object> {
  public TestCaseData with(String key, Object value) {
    this.put(key, value);
    return this;
  }

  public TestCaseData without(String key) {
    this.remove(key);
    return this;
  }

  public TestCaseData clone() {
    var data = new TestCaseData();
    for (String key : this.keySet()) {
      data.put(key, this.get(key));
    }
    return data;
  }
}
