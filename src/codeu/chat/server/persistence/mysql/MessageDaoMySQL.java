// This class defines all the methods needed to save Message information
// to the database and retrieve Message information from the database.

package codeu.chat.server.persistence.mysql;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.server.persistence.dao.MessageDao;
import codeu.chat.util.Logger;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MessageDaoMySQL implements MessageDao {

  // save a new message to the database
  public void saveMessage(Message message) throws SQLException {

    String query = "insert into conversation_messages (id, next_id, previous_id, " +
                   "time_created, author_id, content) values(?, ?, ?, ?, ?, ?)";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      // need formatter to store a time into a database
      SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      preparedStmt.setString(1, message.id.toString());
      preparedStmt.setString(2, message.next.toString());
      preparedStmt.setString(3, message.previous.toString());
      preparedStmt.setString(4, datetimeFormatter.format(new Date(message.creation.inMs())));
      preparedStmt.setString(5, message.author.toString());
      preparedStmt.setString(6, message.content);

      preparedStmt.executeUpdate();

      conn.close();

    } catch(SQLException ex) {
       throw ex;
    }
  }

  // update a conversation's next and previous messages in the database
  public void updateMessage(Message message, Uuid next, Uuid previous) throws SQLException {
    try {
      String query = "update conversation_messages set next_id = ?, previous_id = ? where id = ?";
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, next.toString());
      preparedStmt.setString(2, previous.toString());
      preparedStmt.setString(3, message.id.toString());

      preparedStmt.executeUpdate();
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }

  // retrieve a message from the database by id
  public Message getMessage(Uuid id) throws SQLException {

    Message message = null;
    String query = "select id, next_id, previous_id, time_created, author_id, " +
                   "content from conversation_messages where id = ?;";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString(1, id.toString());
      ResultSet rset = preparedStmt.executeQuery();
      if (rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        message = new Message(Uuid.fromToString(rset.getString("id")),
                              Uuid.fromToString(rset.getString("next_id")),
                              Uuid.fromToString(rset.getString("previous_id")),
                              time,
                              Uuid.fromToString(rset.getString("author_id")),
                              rset.getString("content"));
      }
    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return message;
  }

  // retrieve an ArrayList of all the messages from the database
  public ArrayList<Message> getAllMessages() throws SQLException {

    ArrayList<Message> messages = new ArrayList<Message>();
    String query = "select * from conversation_messages";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();

      ResultSet rset = stmt.executeQuery(query);

      while(rset.next()) {
        long ms = rset.getTime("time_created").getTime();
        Time time = Time.fromMs(ms);
        Message message = new Message(Uuid.fromToString(rset.getString("id")),
                                      Uuid.fromToString(rset.getString("next_id")),
                                      Uuid.fromToString(rset.getString("previous_id")),
                                      time,
                                      Uuid.fromToString(rset.getString("author_id")),
                                      rset.getString("content"));

        messages.add(message);
      }

    } catch (SQLException ex) {
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return messages;
  }

  // delete message from the database
  // this method was created for testing purposes to be able to keep database clean
  public void deleteMessage(Message message) throws SQLException {

    String query = "delete from conversation_messages where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);

      preparedStmt.setString(1, message.id.toString());

      preparedStmt.executeUpdate();

      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }
}
