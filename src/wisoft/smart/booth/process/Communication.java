package wisoft.smart.booth.process;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Communication {
  private SocketChannel socketChannel;
  private Charset charset;
  private  Median boothMedian;
  private Median kioskMedian;

  public Communication(SocketChannel socketChannel) {
    this.boothMedian = new Median(5);
    this.kioskMedian = new Median(5);
    this.socketChannel = socketChannel;
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
    try {
      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
      String code = jsonObject.get("code").toString();

      switch (code) {
        case "register":
         log("서비스 등록결과 : " + jsonObject.get("response"));
          break;

        case "median":
         if (jsonObject.get("service").equals("booth")) {
           boothMedian.setValue(Integer.parseInt(jsonObject.get("value").toString()));

           if (boothMedian.isMedian()) {
             System.out.println("부스 중앙값 > " + boothMedian.getMedian());
           } else {
             System.out.println("부스값 센싱중...");
           }

         } else {
           kioskMedian.setValue(Integer.parseInt(jsonObject.get("value").toString()));

           if (kioskMedian.isMedian()) {
             System.out.println("키오스크 중앙값 > " + kioskMedian.getMedian());
           } else {
             System.out.println("키오스크 센싱중...");
           }
         }
          break;

        case "kiosk":
          log("kiosk : " + jsonObject.get("smoking"));
          break;

        default:
          log("알려지지 않은 데이터가 접속하였습니다");
          log(msg);
      }

    } catch (ParseException e) {
      e.printStackTrace();
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
