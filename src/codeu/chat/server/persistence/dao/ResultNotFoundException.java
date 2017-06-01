
package codeu.chat.server.persistence.dao;

// this exception is thrown when
public class ResultNotFoundException extends Exception {
  private static final long serialVersionUID = 40939;
  public ResultNotFoundException() {
    super();
  }
  public ResultNotFoundException(String message) {
    super(message);
  }
}
