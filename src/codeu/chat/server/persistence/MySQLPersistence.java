package codeu.chat.server.persistence;

import java.util.Collection;
import java.util.ArrayList;

import codeu.chat.common.BasicController;
import codeu.chat.common.User;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;

import codeu.chat.server.persistence.mysql.UserDaoMySQL;
import codeu.chat.server.persistence.mysql.ConversationDaoMySQL;
import codeu.chat.server.persistence.mysql.MessageDaoMySQL;

import codeu.chat.server.Model;
import codeu.chat.server.View;

import codeu.chat.util.Uuid;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;

import java.sql.SQLException;


public class MySQLPersistence implements DataPersistence {

  // fields
  UserDaoMySQL userDaoMySQL;
  ConversationDaoMySQL conversationDaoMySQL;
  MessageDaoMySQL messageDaoMySQL;

  // constructor
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

  public void loadUsers(Model model, View view) {
    try {
      ArrayList<User> users = userDaoMySQL.getAllUsers();
      for (User user : users) {
        if (view.findUser(user.id) == null) {   // only load in users that are not already saved
          model.add(user);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void loadConversations(Model model, View view) {
    try {
      ArrayList<Conversation> conversations = conversationDaoMySQL.getAllConversations();
      for (Conversation conversation : conversations) {
        if (view.findConversation(conversation.id) == null) {   // only load in conversations that are not already saved
          model.add(conversation);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  public void loadMessages(Model model, View view) {
    try {
      ArrayList<Message> messages = messageDaoMySQL.getAllMessages();
      for (Message message : messages) {
        if (view.findMessage(message.id) == null) {   // only load in conversations that are not already saved
          model.add(message);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

}
