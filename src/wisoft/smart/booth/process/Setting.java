package wisoft.smart.booth.process;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Setting {
  private SocketChannel socketChannel;

  public Setting() {
    this.socketChannel = null;
  }

  public boolean connectServer(String ip, int port) {
    try {
      socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(true);
      socketChannel.connect(new InetSocketAddress(ip, port));
      return true;
    } catch (IOException e) {
      log("연결과정에서 오류가 발생하여 다음 에러코드를 확인후 다시 시도하세요.");
      log("Error code : " + e);
      close();
      System.exit(1);
      return false;
    }
  }

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  private void close() {
    if (!socketChannel.isConnected()) {
      try {
        socketChannel.close();
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
