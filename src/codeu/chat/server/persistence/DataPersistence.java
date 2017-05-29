package codeu.chat.server.persistence;

import java.util.Collection;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;

import codeu.chat.server.Model;
import codeu.chat.server.View;
import codeu.chat.util.Time;

import codeu.chat.common.User;

import codeu.chat.util.Uuid;
import codeu.chat.util.Logger;

public interface DataPersistence {

  void saveUser(User user);
  void saveMessage(Message message);
  void saveConversation(Conversation conversation);

  void updateConversation(Conversation conversation, Uuid first_message_id, Uuid last_message_id);
  void updateMessage(Message message, Uuid next, Uuid previous);

  void addConversationUser(Conversation conversation, User user);

  void restoreState(Model model);
}
