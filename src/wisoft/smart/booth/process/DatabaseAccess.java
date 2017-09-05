package wisoft.smart.booth.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccess {
  private static Connection connection = null;

  public void init() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Connection setConnection() {
    String url = "jdbc:postgresql://192.168.0.67:5432/smart_booth";
    String username = "space";
    String password = "media";

    try {
      connection = DriverManager.getConnection(url, username, password);
      System.out.println("    ----- DATABASE ACCESS!!");
    } catch (SQLException e) {
      System.out.println("데이터 베이스 접속에 실패했습니다.");
      System.out.println("Error : " + e);
    }
    return connection;
  }
}
