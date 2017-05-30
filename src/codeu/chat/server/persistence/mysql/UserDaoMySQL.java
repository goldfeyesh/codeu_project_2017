// This class defines all the methods needed to save User information
// to the database and retrieve User information from the database.

package codeu.chat.server.persistence.mysql;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.User;
import codeu.chat.server.persistence.dao.ResultNotFoundException;
import codeu.chat.server.persistence.dao.UserDao;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
public class UserDaoMySQL implements UserDao {

  // save a new user to the database
  public void saveUser(User user) throws SQLException {

    String query = "insert into users (id, username, time_created) values(?, ?, ?)";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      // need formatter to store a time into database
      SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      preparedStmt.setString (1, user.id.toString());
      preparedStmt.setString (2, user.name);
      preparedStmt.setString (3, datetimeFormatter.format(new Date(user.creation.inMs())));

      preparedStmt.executeUpdate();

      conn.close();
    } catch(SQLException ex) {
       ex.printStackTrace();
       throw ex;
    }
  }

  // retrieve a user from the database by id
  public User getUser(Uuid id) throws SQLException, ResultNotFoundException {

    User user = null;
    String query = "select id, username, time_created from users where id = ?;";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, id.toString());

      ResultSet rset = preparedStmt.executeQuery();

      if (rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);

        user = new User(Uuid.fromToString(rset.getString("id")), rset.getString("username"), time);
      }
      else {
        throw new ResultNotFoundException("Could not find the user with id:" + id.toString());
      }
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return user;
  }

  // retrieve an ArrayList of all the users from the database.
  public ArrayList<User> getAllUsers() throws SQLException{

    ArrayList<User> users = new ArrayList<User>();
    String query = "select * from users";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();

      ResultSet rset = stmt.executeQuery(query);

      while(rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        User user = new User(Uuid.fromToString(rset.getString("id")), rset.getString("username"), time);

        users.add(user);
      }
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return users;
  }

  // remove a user from the database
  // this method was created for testing purposes to be able to keep database clean
  public void deleteUser(User user) throws SQLException {

    String query = "delete from users where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, user.id.toString());

      preparedStmt.executeUpdate();

      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}
