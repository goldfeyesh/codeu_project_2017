// this class uses the Data Access Objects specific to MySQL to persist data

package codeu.chat.server.persistence;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.User;
import codeu.chat.server.Model;
import codeu.chat.server.persistence.mysql.ConversationDaoMySQL;
import codeu.chat.server.persistence.mysql.MessageDaoMySQL;
import codeu.chat.server.persistence.mysql.UserDaoMySQL;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLPersistence implements DataPersistence {

  UserDaoMySQL userDaoMySQL;
  ConversationDaoMySQL conversationDaoMySQL;
  MessageDaoMySQL messageDaoMySQL;

  public MySQLPersistence() {
    userDaoMySQL = new UserDaoMySQL();
    conversationDaoMySQL = new ConversationDaoMySQL();
    messageDaoMySQL = new MessageDaoMySQL();
  }

  public void saveUser(User user) {
    try {
      userDaoMySQL.saveUser(user);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void saveConversation(Conversation conversation) {
    try {
      conversationDaoMySQL.saveConversation(conversation);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void saveMessage(Message message) {
    try {
      messageDaoMySQL.saveMessage(message);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void addConversationUser(Conversation conversation, User user) {
    try {
      conversationDaoMySQL.addConversationUser(conversation, user);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void updateConversation(Conversation conversation, Uuid first_message_id, Uuid last_message_id) {
    try {
      conversationDaoMySQL.updateConversation(conversation, first_message_id, last_message_id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void updateMessage(Message message, Uuid next, Uuid previous) {
    try {
      messageDaoMySQL.updateMessage(message, next, previous);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void loadUsers(Model model) {
    try {
      ArrayList<User> users = userDaoMySQL.getAllUsers();
      for (User user : users) {
        model.add(user);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void loadConversations(Model model) {
    try {
      ArrayList<Conversation> conversations = conversationDaoMySQL.getAllConversations();
      for (Conversation conversation : conversations) {
          model.add(conversation);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  public void loadMessages(Model model) {
    try {
      ArrayList<Message> messages = messageDaoMySQL.getAllMessages();
      for (Message message : messages) {
        model.add(message);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void restoreState(Model model) {
    this.loadUsers(model);
    this.loadConversations(model);
    this.loadMessages(model);
  }
}
