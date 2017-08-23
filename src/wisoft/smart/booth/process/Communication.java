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

  public Communication(SocketChannel socketChannel) {
    this.boothMedian = new Median(5);
    this.kioskMedian = new Median(5);
    this.socketChannel = socketChannel;
    this.json = new Json();
    this.charset = null;
    receive();
  }

  private void receive() {
    Runnable runnable = () -> {
      while (true) {
        try {
          ByteBuffer byteBuffer = ByteBuffer.allocate(100);
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
          close();

        } catch (NullPointerException e) {
          log("비정상적인 오류가 발생했습니다.");
          close();

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

      case "register":
        log("서비스 등록결과 : " + json.getValue(msg, "response"));
        break;

      case "median":
        if (json.getValue(msg, "device").equals("booth")) {
          boothMedian.setValue(json.getValue(msg, "value"));
          send(json.createMedian("device", "booth", boothMedian.getMedian()));
          break;
        }

        kioskMedian.setValue(json.getValue(msg, "value"));
        send(json.createMedian("device", "kiosk", kioskMedian.getMedian()));
        break;

      case "error":
        log("라우터에서 하가(등록)되지 않는 디바이스가 접근했습니다.");
        break;

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
