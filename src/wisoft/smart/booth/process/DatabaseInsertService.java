package wisoft.smart.booth.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsertService {
  public String account(final String username, final String password, final String email, final String tel) {

    final String query = "INSERT INTO account(username, password, email, phone) VALUES(?, ?, ?, ?)";

    try (Connection connection = DatabaseAccess.setConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

      connection.setAutoCommit(false);
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, email);
      ps.setString(4, tel);
      int retValue = ps.executeUpdate();
      connection.commit();

      return String.valueOf(retValue);

    } catch (SQLException e) {
      log("SQLException : " + e.getMessage());
      log("SQLState : " + e.getSQLState());
      return null;
    }
  }

  private void log(String str) {
    System.out.println("--- " + str + " ---");
  }
}
