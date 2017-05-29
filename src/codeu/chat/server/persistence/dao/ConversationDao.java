package codeu.chat.server.persistence.dao;

import java.sql.*;

import java.util.Collection;
import java.util.List;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.util.Time;

import codeu.chat.common.User;

import codeu.chat.util.Uuid;
import codeu.chat.util.Logger;
import codeu.chat.server.persistence.dao.ResultNotFoundException;


public interface ConversationDao {

  void saveConversation(Conversation conversation) throws SQLException, ResultNotFoundException;
  List<Conversation> getAllConversations() throws SQLException;
}