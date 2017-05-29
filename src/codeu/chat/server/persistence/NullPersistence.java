package codeu.chat.server.persistence;

import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.User;
import codeu.chat.util.Uuid;
import codeu.chat.server.Model;
import codeu.chat.server.View;


// minimum implementation does not interact with database.
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

  public void loadUsers(Model model, View view) {
  }

  public void loadConversations(Model model, View view) {
  }

  public  void loadMessages(Model model, View view){
  }

}
