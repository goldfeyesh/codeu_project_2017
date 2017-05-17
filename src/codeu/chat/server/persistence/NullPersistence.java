package codeu.chat.server.persistence;

import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.User;

// minimum implementation does not interact with database.
public class NullPersistence implements DataPersistence {

  public void saveUser(User user){
  }

  public void saveMessage(Message message, Conversation conversation) {
  }

  public void saveConversation(Conversation conversation) {
  }

  public void loadUsers() {
  }

  public void loadConversations() {
  }

  public  void loadMessages(){
  }

}
