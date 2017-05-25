package codeu.chat.server.persistence.dao;

import java.util.Collection;
import java.util.List;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.Time;

import codeu.chat.common.User;

import codeu.chat.common.Uuid;
import codeu.chat.common.Uuids;
import codeu.chat.util.Logger;

public interface MessageDao {

  void saveMessage(Message message);
  List<Message> getAllMessages(Conversation conversation);
}
