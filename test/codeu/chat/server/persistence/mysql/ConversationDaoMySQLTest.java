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
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.util.Uuid;
import codeu.chat.server.persistence.*;
import codeu.chat.server.persistence.mysql.*;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.server.Model;


public final class ConversationDaoMySQLTest {

  private Model model;
  private BasicController controller;
  private DataPersistence persistence;
  private ConversationDaoMySQL conversationDaoMySQL;
  private User user;
  private Conversation conv1;
  private Conversation conv2;
  private Message message;

  @Before
  public void doBefore() {
    model = new Model();
    controller = new Controller(Uuid.NULL, model, persistence);
    conversationDaoMySQL = new ConversationDaoMySQL();
    user = controller.newUser("username");
    conv1 = controller.newConversation("conv1", user.id);
    conv2 = controller.newConversation("conv2", user.id);
    message = controller.newMessage(user.id, conv1.id, "body");
  }

  @Test
  public void testSaveConversation() {
    try {
      conversationDaoMySQL.saveConversation(conv1);
      conversationDaoMySQL.saveConversation(conv2);
      Conversation foundConversation = conversationDaoMySQL.getConversation(conv1.id);
      assertTrue("id of found conversation matches id of saved conversation",
                foundConversation.id.toString().equals(conv1.id.toString()));

      conversationDaoMySQL.deleteConversation(conv1);
      conversationDaoMySQL.deleteConversation(conv2);

    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (ResultNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUpdateConversation() {

    try {
      conversationDaoMySQL.saveConversation(conv1);

      conversationDaoMySQL.updateConversation(conv1, message.id, message.id);

      String firstmsg = conv1.firstMessage.toString();
      String lastmsg = conv1.lastMessage.toString();
      assertTrue("The first_message_id and last_message_id of conv1 are not null and equivalent",
                 firstmsg.equals(lastmsg));

      conversationDaoMySQL.deleteConversation(conv1);

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testGetAllConversations() {
    try {
      conversationDaoMySQL.saveConversation(conv1);
      conversationDaoMySQL.saveConversation(conv2);

      List<Conversation> conversations = conversationDaoMySQL.getAllConversations();
      assertTrue("There are conversations saved in the database.", conversations.size() > 0);

      conversationDaoMySQL.deleteConversation(conv1);
      conversationDaoMySQL.deleteConversation(conv2);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
