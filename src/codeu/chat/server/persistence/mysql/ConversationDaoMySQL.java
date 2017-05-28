package codeu.chat.server.persistence.mysql;
import java.sql.*;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.common.Time;

import codeu.chat.common.User;
import codeu.chat.server.persistence.dao.ConversationDao;
import codeu.chat.server.persistence.dao.ResultNotFoundException;

import codeu.chat.common.Uuid;
import codeu.chat.common.Uuids;
import codeu.chat.util.Logger;

public class ConversationDaoMySQL implements ConversationDao {

  public void saveConversation(Conversation conversation) {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();

      // the mysql insert statement
      String query = "insert into conversation (id, owner_id, time_created, title)"
                   + " values(?, ?, ?, ?)";

      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      preparedStmt.setString(1, conversation.id.toString());
      preparedStmt.setString(2, conversation.owner.toString());
      preparedStmt.setString(3, datetimeFormatter.format(new Date(conversation.creation.inMs())));
      preparedStmt.setString(4, conversation.title);
      preparedStmt.executeUpdate();

      conn.close();

    } catch(SQLException ex) {
       ex.printStackTrace();
    }
  }

  public void deleteConversation(Conversation conversation) throws SQLException {  // for testing purposes, delete conversations
    try {
      String query = "delete from conversation where id = ?";
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString(1, conversation.id.toString());
      int count = preparedStmt.executeUpdate();
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }

  public static void updateConversation(Conversation conversation, Uuid first_message_id, Uuid last_message_id) throws SQLException{
    try {
      String query = "update conversation set first_message_id = ?, last_message_id = ? where id = ?";
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

  public Conversation getConversation(Uuid id) throws SQLException, ResultNotFoundException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      String query = "select id, owner_id, time_created, title from conversation where id = ?;";
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString(1, id.toString());
      ResultSet rset = preparedStmt.executeQuery();
      if (rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        Conversation conversation = new Conversation(Uuids.fromString(rset.getString("id")),
                                             Uuids.fromString(rset.getString("owner_id")),
                                             time, rset.getString("title"));
        return conversation;
      }
      else {
        throw new ResultNotFoundException("Could not find the conversation with id:" + id.toString());
      }
    } catch (SQLException ex) {
      throw ex;
    }
  }

  public List<Conversation> getAllConversations() throws SQLException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      String query = "select * from conversation";

      ResultSet rset = stmt.executeQuery(query);
      ArrayList<Conversation> conversations = new ArrayList<Conversation>();

      while(rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        Conversation conversation = new Conversation(Uuids.fromString(rset.getString("id")),
                                                     Uuids.fromString(rset.getString("owner_id")),
                                                     time, rset.getString("title"));
        conversations.add(conversation);
      }
      return conversations;
    } catch (SQLException ex) {
      throw ex;
    }
  }
}
