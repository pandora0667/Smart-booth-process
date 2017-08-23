package wisoft.smart.booth.process;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json {

  public String getValue(String msg, String key) {
    try {
      JSONParser codeParser = new JSONParser();
      JSONObject codeJson = (JSONObject) codeParser.parse(msg);
      return codeJson.get(key).toString();

    } catch (ParseException e) {
      System.out.println("JSON Parsing failed..");
      return null;
    }
  }

  public String createMedian(String key, String value, String median) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", "median");
    jsonObject.put(key, value);
    jsonObject.put("value", median);

    return jsonObject.toJSONString();

  }
}
