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
public final class MessageDaoMySQLTest {

  private Model model;
  private BasicController controller;
  private DataPersistence persistence;
  private MessageDaoMySQL messageDaoMySQL;
  private User user;
  private Conversation conversation;
  private Message message1;
  private Message message2;

  @Before
  public void doBefore() {
    model = new Model();
    controller = new Controller(Uuid.NULL, model, persistence);
    messageDaoMySQL = new MessageDaoMySQL();
    user = controller.newUser("user");
    conversation = controller.newConversation("conversation", user.id);
    message1 = controller.newMessage(user.id, conversation.id, "message1");
    message2 = controller.newMessage(user.id, conversation.id, "message2");
  }

  @Test
  public void testSaveMessage() {
    try {
      messageDaoMySQL.clearMessages();
      messageDaoMySQL.saveMessage(message1);
      messageDaoMySQL.saveMessage(message2);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testUpdateMessage() {
    try {
      MessageDaoMySQL.updateMessage(message1, message2.id, message2.id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testGetAllMessages() {
    try {
      List<Message> messages = messageDaoMySQL.getAllMessages();

      assertTrue("There are 2 messages saved in the database.", messages.size() == 2);

      messageDaoMySQL.clearMessages();

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
