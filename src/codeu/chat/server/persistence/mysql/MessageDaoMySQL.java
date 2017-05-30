package codeu.chat.server.persistence.mysql;
import java.sql.*;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import codeu.chat.common.BasicController;
import codeu.chat.common.Conversation;
import codeu.chat.common.Message;
import codeu.chat.common.RawController;
import codeu.chat.util.Time;

import codeu.chat.common.Message;
import codeu.chat.server.persistence.dao.MessageDao;

import codeu.chat.util.Uuid;
import codeu.chat.util.Logger;

public class MessageDaoMySQL implements MessageDao {


  public void saveMessage(Message message) throws SQLException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();

      // the mysql insert statement
      String query = "insert into conversation_messages (id, next_id, previous_id, time_created, author_id, content)"
                   + " values(?, ?, ?, ?, ?, ?)";

      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = conn.prepareStatement(query);

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
       ex.printStackTrace();
       throw ex;
    }
  }

  public void deleteMessage(Message message) throws SQLException {  // for testing purposes, delete messages
    String query = "delete from conversation_messages where id = ?";
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.setString(1, message.id.toString());
      int count = preparedStmt.executeUpdate();
      conn.close();
    } catch (SQLException ex) {
      throw ex;
    }
  }

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

  public Message getMessage(Uuid id) throws SQLException {
    Message message = null;
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      String query = "select id, next_id, previous_id, time_created, author_id, content from conversation_messages where id = ?;";
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

  public ArrayList<Message> getAllMessages() throws SQLException {
    ArrayList<Message> messages = new ArrayList<Message>();
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      String query = "select * from conversation_messages";

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
      ex.printStackTrace();
      throw ex;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return messages;
  }

  public void clearMessages() throws SQLException {
    try {
      Connection conn = MySQLConnectionFactory.getInstance().getConnection();
      Statement stmt = conn.createStatement();
      String query = "delete from conversation_messages";

      stmt.executeUpdate(query);
    } catch (SQLException ex) {
      throw ex;
    }
  }
}
