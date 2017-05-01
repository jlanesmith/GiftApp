/**
 * Project: TextBased
 * File: Getters.java
 */

/**
 * @author Jonathan Lane-Smith
 */
public class Getters {

  public static int getInput(int first, int last) {

    int input = TextIO.getlnInt();
    boolean boolcheck = true;

    while (boolcheck) {

      if (input >= first && input <= last)
        boolcheck = false;
      else {
        if (first != last) {
          System.out.println("Please enter an integer between " + first + " and " + last + ".");
          input = TextIO.getlnInt();
        } else {
          System.out.println("Please enter " + first);
          input = TextIO.getlnInt();
        }
      }
    }
    return input;
  }

  public static int getNumUsers(Accounts[] users) {

    int i = 0;

    try {
      while (true) {
        String test = users[i].username;
        if (test.equals(null))
          return i;
        i++;
      }
    } catch (Exception e) {
      return i;
    }
  }

  public static int getNumEntries(Accounts[] users, Lists[] lists, Accounts mainUser) {

    int num = getAccountNum(users, mainUser);
    int i = 0;

    try {
      while (true) {
        String test = lists[num].wishlist[i];
        if (test.equals(null))
          return i;
        i++;
      }
    } catch (NullPointerException e) {
      return i;
    }
  }

  public static int getAccountNum(Accounts[] users, Accounts mainUser) {

    for (int i = 0; i < 100; i++) {
      if (users[i].username.equals(mainUser.username))
        return i;
    }
    return 0;
  }

  public static Accounts getAccount(Accounts[] users, String username) {

    for (int i = 0; i < getNumUsers(users); i++) {
      if (username.equals(users[i].username)) {
        return users[i];
      }
    }
    return null;
  }

  public static Accounts getAccount(Accounts[] users, String username, String password) {

    for (int i = 0; i < getNumUsers(users); i++) {
      if (users[i].username.equals(username) && users[i].password.equals(password)) {
        return users[i];
      }
    }
    return null;
  }
}