package wisoft.smart.booth.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSelectService {

  public boolean accountVerification(String username, String password) {
   String query = "SELECT password FROM account WHERE username = ?";

   try (Connection connection = DatabaseAccess.setConnection();
        PreparedStatement ps = connection.prepareStatement(query)) {

     connection.setAutoCommit(false);
     ps.setString(1, username);
     try (ResultSet rs = ps.executeQuery()) {
       rs.next();
       return password.equals(rs.getString(1));
     }
   } catch (SQLException e) {
     log("SQLException : " + e.getMessage());
     log("SQLState : " + e.getSQLState());
     return false;
   }
  }

  private void log(String str) {
    System.out.println("--- " + str + " ---");
  }
}
