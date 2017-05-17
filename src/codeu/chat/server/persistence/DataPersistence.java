package codeu.chat.server.persistence;

import java.util.Collection;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.Time;

import codeu.chat.common.User;

import codeu.chat.common.Uuid;
import codeu.chat.common.Uuids;
import codeu.chat.util.Logger;

public interface DataPersistence {

  void saveUser(User user);
  void saveMessage(Message message, Conversation conversation);
  void saveConversation(Conversation conversation);
  void loadUsers();
  void loadConversations();
  void loadMessages();

}
