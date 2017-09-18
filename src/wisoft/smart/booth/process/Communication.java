package wisoft.smart.booth.process;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Communication {
  private SocketChannel socketChannel;
  private Charset charset;
  private Median boothMedian;
  private Median kioskMedian;
  private Json json;
  private DatabaseInsertService insertService;
  private DatabaseSelectService selectService;

  public Communication(SocketChannel socketChannel) {
    this.boothMedian = new Median(5);
    this.kioskMedian = new Median(5);
    this.socketChannel = socketChannel;
    this.json = new Json();
    this.insertService = new DatabaseInsertService();
    this.selectService = new DatabaseSelectService();
    this.charset = null;
    receive();
  }

  private void receive() {
    Runnable runnable = () -> {
      while (true) {
        try {
          ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
          int isClose = socketChannel.read(byteBuffer);
          if (isClose == -1) {
            throw new Exception();
          }

          byteBuffer.flip();
          String msg = charset.decode(byteBuffer).toString();
          serviceCode(msg);

        } catch (IOException e) {
          log("데이터 수신중 다음과 같은 오류가 발생했습니다.");
          log("Error code : " + e);

        } catch (NullPointerException e) {
          log("비정상적인 오류가 발생했습니다.");
          log("Error code : " + e);

        } catch (Exception e) {
          log("라우터와 연결이 종료되어 서비스를 종료합니다.");
          log("Error code : " + e);
          close();
        }
      }
    };
    new Thread(runnable).start();
  }

  public void send(String msg) {
    try {
      charset = Charset.forName("UTF-8");
      ByteBuffer byteBuffer = charset.encode(msg);
      socketChannel.write(byteBuffer);
    } catch (IOException e) {
      log("데이터 전송중 오류가 발생했습니다.");
      log("Error code : " + e);
    }
  }

  private void serviceCode(String msg) {
    switch (json.getValue(msg, "code")) {

      case "register":  // 라우터 등록
        log("서비스 등록결과 : " + json.getValue(msg, "response"));
        break;

      case "median":  // 중앙값 처리
        if (json.getValue(msg, "device").equals("booth")) {
          boothMedian.setValue(json.getValue(msg, "value"));
          send(json.getMedian("device", "booth", boothMedian.getMedian()));
          break;
        }

        kioskMedian.setValue(json.getValue(msg, "value"));
        send(json.getMedian("device", "kiosk", kioskMedian.getMedian()));
        break;

      case "login": // 로그인 확인
        if (selectService.accountVerification(json.getValue(msg, "username"), json.getValue(msg, "password"))) {
          log("로그인 요청이 성공했습니다.");
          send(json.getAccount("result", "login", "true"));
          break;
        }
        send(json.getAccount("result", "login", "false"));
        break;

      case "sign": // 회원가입 요청시 DB 저장
        String retValue = insertService.account(json.getValue(msg, "username"), json.getValue(msg, "password"), json.getValue(msg, "email"), json.getValue(msg, "tel"));
        if (retValue != null) {
          log(retValue + " 건의 사항이 처리되었습니다.");
          send(json.getAccount("result", "sign",  retValue));
          break;
        }
        send(json.getAccount("result", "sign", "false"));
        break;

      case "error":
        log("라우터에서 하가(등록)되지 않는 디바이스가 접근했습니다.");
        break;

        //TODO File 설정 (특정 다렉토리에 있는 파일 검색후 데이터베이스에 저장 및 전송
      default:
        log("알려지지 않은 데이터가 수신되었습니다");
        log(msg);
    }
  }

  private void close() {
    if (socketChannel.isConnected()) {
      try {
        socketChannel.close();
        System.exit(0);
      } catch (IOException e) {
        log("Socket channel service close error : " + e);
      }
    }
  }

  private void log(String str) {
    System.out.println();
    System.out.println("**  " + str + "  **");
  }
}
