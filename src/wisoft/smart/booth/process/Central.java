package wisoft.smart.booth.process;

import org.json.simple.JSONObject;


public class Central {
  private Setting setting;
  private Communication communication;

  public Central() {
    this.setting = new Setting();
    this.communication = null;
  }
  public boolean settingNetwork(String ip, int port) {
    return setting.connectServer(ip, port);
  }

  public void registerRouter(String key, String value) {
    JSONObject json = new JSONObject();
    json.put(key, value);
    json.put("service", "process");
    communication = new Communication(setting.getSocketChannel());
    String serviceRegister = json.toJSONString();
    communication.send(serviceRegister);
  }
}
