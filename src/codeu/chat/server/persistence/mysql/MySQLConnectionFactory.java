// creates connection to MySQL database
// this class is a singleton so that other classes that need can make these database connections.

package codeu.chat.server.persistence.mysql;
import java.sql.*;

public class MySQLConnectionFactory {
  private String hostname;
  private int port;
  private String username;
  private String password;
  private String dbname;
  private static MySQLConnectionFactory mySQLConnection = null;

  private MySQLConnectionFactory() {
  }

  private void setMySQLParams() {
    this.setMySQLParams("localhost", 3306, "myuser", "aliceli1", "mydb");
  }

  private void setMySQLParams(String hostname, int port, String username, String password, String dbname) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
    this.dbname = dbname;
  }

  public static MySQLConnectionFactory getInstance() {
    if (mySQLConnection == null) {
      mySQLConnection = new MySQLConnectionFactory();
      mySQLConnection.setMySQLParams();
    }
    return mySQLConnection;
  }

  public Connection getConnection() throws SQLException {
    try {
      Connection conn = DriverManager.getConnection(
        "jdbc:mysql://" + hostname + ":" + Integer.toString(port) + "/" + dbname + "?useSSL=false", username, password);
         // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
      return conn;
    } catch(SQLException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
