package wisoft.smart.booth.process;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {

  public String getValue(final String msg, final String key) {
    try {
      JSONParser codeParser = new JSONParser();
      JSONObject codeJson = (JSONObject) codeParser.parse(msg);
      return codeJson.get(key).toString();

    } catch (ParseException e) {
      System.out.println(e);
      System.out.println("JSON Parsing failed..");
      return null;
    }
  }

  public String getMedian(final String key, final String value, final String median) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", "median");
    jsonObject.put(key, value);
    jsonObject.put("value", median);

    return jsonObject.toJSONString();
  }

  public String getAccount(final String code, final String service, final String status) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    jsonObject.put("service", service);
    jsonObject.put("status", status);

    return  jsonObject.toJSONString();
  }
}

