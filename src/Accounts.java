/**
 * Project: TextBased
 * File: Accounts.java
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jonathan Lane-Smith
 */
public class Accounts {

  private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");

  public String username;
  public String password;
  public String timeAccountCreated;

  Accounts(String username, String password) {
    this.username = username;
    this.password = password;

    Date date = new Date();
    this.timeAccountCreated = dateFormat.format(date);
  }

  Accounts(String username, String password, String timeCreated) {
    this.username = username;
    this.password = password;
    this.timeAccountCreated = timeCreated;
  }
}
