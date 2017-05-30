// in case user does not indicate that they want to persist data in a database,
// null implementation does not interact with database.

package codeu.chat.server.persistence;

import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.User;
import codeu.chat.server.Model;
import codeu.chat.server.View;
import codeu.chat.util.Uuid;

public class NullPersistence implements DataPersistence {

  public void saveUser(User user){
  }

  public void saveMessage(Message message) {
  }

  public void saveConversation(Conversation conversation) {
  }

  public void updateConversation(Conversation conversation, Uuid first_message_id, Uuid last_message_id) {
  }

  public void updateMessage(Message message, Uuid next, Uuid previous){
  }

  public void addConversationUser(Conversation conversation, User user){

  }


  public void restoreState(Model model) {
  }

}
