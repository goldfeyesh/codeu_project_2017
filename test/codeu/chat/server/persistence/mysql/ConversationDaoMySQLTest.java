package codeu.chat.server.persistence.mysql;

import java.sql.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import codeu.chat.server.Controller;
import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.Conversation;
import codeu.chat.common.Uuids;
import codeu.chat.server.persistence.*;
import codeu.chat.server.persistence.mysql.*;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.server.Model;


public final class ConversationDaoMySQLTest {

  private Model model;
  private BasicController controller;
  private DataPersistence persistence;
  private ConversationDaoMySQL conversationDaoMySQL;

  @Before
  public void doBefore() {
    model = new Model();
    controller = new Controller(Uuids.NULL, model, persistence);
    conversationDaoMySQL = new ConversationDaoMySQL();
  }

  @Test
  public void testSaveConversation() {
    Conversation conversation = null;
    try {
      conversation = controller.newConversation("conversation");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    try {
      conversationDaoMySQL.deleteConversation(conversation);
    } catch (SQLException ex) {
    }
    try {
      conversationDaoMySQL.saveConversation(conversation);
      Conversation foundConversation = conversationDaoMySQL.getConversation(conversation.id);
      System.out.println(foundConversation.id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (ResultNotFoundException e) {
      e.printStackTrace();
    }
    try {
      conversationDaoMySQL.deleteConversation(conversation);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
