package codeu.chat.server.persistence.mysql;

import java.sql.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.List;

import codeu.chat.server.Controller;
import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.User;
import codeu.chat.util.Uuid;
import codeu.chat.server.persistence.*;
import codeu.chat.server.persistence.mysql.*;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.server.Model;

public final class UserDaoMySQLTest {

  private Model model;
  private BasicController controller;
  private DataPersistence persistence;
  private UserDaoMySQL userDaoMySQL;
  private User user;
  private User user2;

  @Before
  public void doBefore() {
    model = new Model();
    controller = new Controller(Uuid.NULL, model, persistence);
    userDaoMySQL = new UserDaoMySQL();
    user = controller.newUser("user");
    user2 = controller.newUser("user2");
  }

  @Test
  public void testSaveUser() {
    try {
      userDaoMySQL.saveUser(user);
      userDaoMySQL.saveUser(user2);

      User foundUser = userDaoMySQL.getUser(user.id);
      assertTrue("found user's id matches saved user's id",
                 foundUser.id.toString().equals(user.id.toString()));

      userDaoMySQL.deleteUser(user);
      userDaoMySQL.deleteUser(user2);

    } catch (SQLException ex) {
      System.out.println("SQLException");
      ex.printStackTrace();
    } catch (ResultNotFoundException e) {
      System.out.println("ResultNotFoundException");
      e.printStackTrace();
    }
  }

  @Test
  public void testGetAllUsers() {
    try {
      userDaoMySQL.saveUser(user);
      userDaoMySQL.saveUser(user2);

      List<User> users = userDaoMySQL.getAllUsers();

      assertTrue("There are users saved in the database.", users.size() > 0);
      
      userDaoMySQL.deleteUser(user);
      userDaoMySQL.deleteUser(user2);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
