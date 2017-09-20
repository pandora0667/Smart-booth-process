package wisoft.smart.booth.process;

import org.json.simple.JSONObject;


public class Central {
  private Setting setting;
  private Communication communication;

  public Central() {
    this.setting = new Setting();
    this.communication = null;
  }
  public boolean settingNetwork(final String ip, final int port) {
    return setting.connectServer(ip, port);
  }

  public void registerRouter(final String key, final String value) {
    JSONObject json = new JSONObject();
    json.put(key, value);
    json.put("service", "process");
    communication = new Communication(setting.getSocketChannel());
    communication.send(json.toJSONString());
  }


}
