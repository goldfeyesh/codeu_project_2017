// This class defines all the methods needed to save Conversation information
// to the database and retrieve Conversation information from the database.

package codeu.chat.server.persistence.mysql;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.User;
import codeu.chat.server.persistence.dao.ConversationDao;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ConversationDaoMySQL implements ConversationDao {

  // save a new conversation to the database
  public void saveConversation(Conversation conversation) throws SQLException {

    String query = "insert into conversation (id, owner_id, time_created, title)" +
                   " values(?, ?, ?, ?)";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      // need formatter to store a time into database
      SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      preparedStmt.setString(1, conversation.id.toString());
      preparedStmt.setString(2, conversation.owner.toString());
      preparedStmt.setString(3, datetimeFormatter.format(new Date(conversation.creation.inMs())));
      preparedStmt.setString(4, conversation.title);

      preparedStmt.executeUpdate();

      conn.close();
    } catch(SQLException ex) {
      throw ex;
    }
  }

  // update a conversation's first and last messages in the database
  public void updateConversation(Conversation conversation, Uuid first_message_id, Uuid last_message_id) throws SQLException{

    String query = "update conversation set first_message_id = ?, last_message_id = ? where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, first_message_id.toString());
      preparedStmt.setString(2, last_message_id.toString());
      preparedStmt.setString(3, conversation.id.toString());

      preparedStmt.executeUpdate();

      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }

  // conversations keep lists of users who participated in them: this method adds a user to a conversation
  public void addConversationUser(Conversation conversation, User user) throws SQLException {

    String query = "insert into conversation_users (user_id, conversation_id) values(?, ?)";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, user.id.toString());
      preparedStmt.setString(2, conversation.id.toString());

      preparedStmt.executeUpdate();

      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }

  // retrieve a conversation from the database by id
  public Conversation getConversation(Uuid id) throws SQLException {

    Conversation conversation = null;
    String query = "select id, owner_id, time_created, title, first_message_id, " +
                   "last_message_id from conversation where id = ?;";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, id.toString());

      ResultSet rset = preparedStmt.executeQuery();

      if (rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);

        conversation = new Conversation(Uuid.fromToString(rset.getString("id")),
                                        Uuid.fromToString(rset.getString("owner_id")),
                                        time,
                                        rset.getString("title"));

        String firstMsgId = rset.getString("first_message_id");
        String lastMsgId = rset.getString("last_message_id");

        // set conversation's first and last messages
        conversation.firstMessage = firstMsgId != null ? Uuid.fromToString(firstMsgId) : null;
        conversation.lastMessage = lastMsgId != null ? Uuid.fromToString(lastMsgId) : null;
      }
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return conversation;
  }

  // retrieve an ArrayList of all the conversations from the database.
  public ArrayList<Conversation> getAllConversations() throws SQLException {

    ArrayList<Conversation> conversations = new ArrayList<Conversation>();
    String query = "select * from conversation";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();

      ResultSet rset = stmt.executeQuery(query);

      while(rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        Conversation conversation = new Conversation(Uuid.fromToString(rset.getString("id")),
                                                     Uuid.fromToString(rset.getString("owner_id")),
                                                     time,
                                                     rset.getString("title"));

        // set conversation's first and last messages
        String firstMsgId = rset.getString("first_message_id");
        String lastMsgId = rset.getString("last_message_id");

        conversation.firstMessage = firstMsgId != null ? Uuid.fromToString(firstMsgId) : null;
        conversation.lastMessage = lastMsgId != null ? Uuid.fromToString(lastMsgId) : null;

        conversations.add(conversation);
      }
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return conversations;
  }

  // remove a conversation from the database
  // this method was created for testing purposes to be able to keep database clean
  public void deleteConversation(Conversation conversation) throws SQLException {

    String query = "delete from conversation where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement stmt = conn.prepareStatement(query);

      stmt.setString(1, conversation.id.toString());

      stmt.executeUpdate();

      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }
}
