package codeu.chat.server.persistence.mysql;
import java.sql.*;
import java.io.IOException;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.util.Time;

import codeu.chat.common.User;
import codeu.chat.server.persistence.dao.UserDao;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.util.Uuid;
import codeu.chat.util.Logger;

public class UserDaoMySQL implements UserDao {


  public void saveUser(User user) throws SQLException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();

      // the mysql insert statement
      String query = "insert into users (id, username, time_created) values(?, ?, ?)";

      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = conn.prepareStatement(query);

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

  public void deleteUser(User user) throws SQLException {  // for testing purposes, delete users
    String query = "delete from users where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString(1, user.id.toString());
      int count = preparedStmt.executeUpdate();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  public User getUser(Uuid id) throws SQLException, ResultNotFoundException {
    User user = null;
    try {

      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      String query = "select id, username, time_created from users where id = ?;";
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
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return user;
  }

  public List<User> getAllUsers() throws SQLException{
    ArrayList<User> users = new ArrayList<User>();
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      String query = "select * from users";

      ResultSet rset = stmt.executeQuery(query);

      while(rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        User user = new User(Uuid.fromToString(rset.getString("id")), rset.getString("username"), time);
        users.add(user);
      }
      return users;
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return users;
  }

  public void clearUsers() throws SQLException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      String query = "delete from users";

      stmt.executeUpdate(query);
    } catch (SQLException ex) {
      throw ex;
    }
  }
}
