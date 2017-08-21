package wisoft.smart.booth.process;

public class Main {
  private final static String ip = "203.230.100.177";
  private final static int port = 5001;

  public static void main(String[] args) {
    System.out.println("|----------------------------------------------|");
    System.out.println("|           Welcome starting process....       |");
    System.out.println("|             Space Media Contents             |");
    System.out.println("|                                              |");
    System.out.println("|                        - Smart smoke booth - |");
    System.out.println("|                        - Project by swlee -  |");
    System.out.println("|----------------------------------------------|");

    System.out.println();
    System.out.println(" -----  Please run the router first -----");
    System.out.println(" -----  Try connect router " + ip + ":" + port + " -----");
    Central central = new Central();

    if (central.settingNetwork(ip, port)) {
      System.out.println("연결 성공");
      System.out.println("라우터에 Process 등록을 시도합니다.");

      central.registerRouter("code", "register");
    }
  }
}