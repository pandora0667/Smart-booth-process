package wisoft.smart.booth.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsertService {
  public int account(String username, String password, String email, String tel) {

    String query = "INSERT INTO account(username, password, email, phone) VALUES(?, ?, ?, ?)";

    try (Connection connection = DatabaseAccess.setConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      connection.setAutoCommit(false);
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, email);
      ps.setString(4, tel);
      int retValue = ps.executeUpdate();
      connection.commit();

      return retValue;

    } catch (SQLException e) {
      log("SQLException : " + e.getMessage());
      log("SQLState : " + e.getSQLState());
      return -1;
    }
  }

  private void log(String str) {
    System.out.println("--- " + str + " ---");
  }
}
