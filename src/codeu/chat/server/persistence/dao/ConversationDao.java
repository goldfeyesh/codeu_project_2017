// this interface defines which methods a Data Access Object should have to
// store conversations into database and get conversations from the database.

package codeu.chat.server.persistence.dao;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.User;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;
import java.lang.Exception;
import java.util.Collection;
import java.util.List;

public interface ConversationDao {

  void saveConversation(Conversation conversation) throws Exception;
  List<Conversation> getAllConversations() throws Exception;
}
