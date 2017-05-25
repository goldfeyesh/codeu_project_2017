package codeu.chat.server.persistence.mysql;

import java.sql.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import codeu.chat.server.Controller;
import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.User;
import codeu.chat.common.Uuids;
import codeu.chat.server.persistence.*;
import codeu.chat.server.persistence.mysql.*;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.server.Model;


public final class UserDaoMySQLTest {

  private Model model;
  private BasicController controller;
  private DataPersistence persistence;
  private UserDaoMySQL userDaoMySQL;

  @Before
  public void doBefore() {
    model = new Model();
    controller = new Controller(Uuids.NULL, model, persistence);
    userDaoMySQL = new UserDaoMySQL();
  }

  @Test
  public void testSaveUser() {
    User user = null;
    try {
      user = controller.newUser("user");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    try {
      userDaoMySQL.deleteUser(user);
    } catch (SQLException ex) {
    }
    try {
      userDaoMySQL.saveUser(user);
      User foundUser = userDaoMySQL.getUser(user.id);
      System.out.println(foundUser.id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (ResultNotFoundException e) {
      e.printStackTrace();
    }
    try {
      userDaoMySQL.deleteUser(user);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
