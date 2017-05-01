/**
 * Project: TextBased
 * File: InputOutput.java
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Jonathan Lane-Smith
 */
public class InputOutput {
  
  public static String encode(String message) {

    char[] array = message.toCharArray();

    String newMessage = "";
    int length = array.length;
    for (int i = 0; i < length; i++) {
      newMessage += (char) ((char) (array[i] + 150));
    }
    return (newMessage + (char) (300));
  }

  public static String decode(String message) {
  
    char[] array = message.toCharArray();
    String newMessage = "";
  
    if (array.length == 0)
      return "";
    else {
      for (int i = 0; i < array.length; i++) {
        char character = array[i];
        if (character != (char) 300)
          newMessage += (char) (character - 150);
        else
          return newMessage;
      }
    }
    return newMessage;
  }

  public static String shorten(String fullString, String partString) {

    int length = partString.length();
    try {
      return fullString.substring(length + 1);
    } catch (Exception e) {
      return "";
    }
  }

  public static void writeFiles(Accounts[] users, Lists[] lists, int numusers) throws IOException {
  
    File outUsers = new File("users.txt");
    File outLists = new File("lists.txt");
    FileWriter fUsers = new FileWriter(outUsers);
    FileWriter fLists = new FileWriter(outLists);
    PrintWriter pUsers = new PrintWriter(fUsers);
    PrintWriter pLists = new PrintWriter(fLists);
    TextIO.writeStream(pUsers);
  
    userLabel: for (int i = 0; i < numusers; i++) {
      if (users[i].username != null) {
        TextIO.put(encode(users[i].username));
        TextIO.put(encode(users[i].password));
        TextIO.put(encode(users[i].timeAccountCreated));
      } else {
        break userLabel;
      }
    }
  
    TextIO.writeStream(pLists);
  
    for (int i = 0; i < numusers; i++) {
  
      wishlistLabel: for (int j = 0;; j++) {
  
        if (lists[i].wishlist[j] != null) {
          TextIO.put(encode(lists[i].wishlist[j]));
          if (lists[i].givers[j] == null) {
            TextIO.put(encode("null"));
          } else {
            TextIO.put(encode(lists[i].givers[j].username));
          }
          TextIO.put(encode(lists[i].dates[j]));
        } else
          break wishlistLabel;
      }
      TextIO.put(encode("0"));
    }
    pUsers.close();
    pLists.close();
  }

  public static int readFiles(Scanner sUsers, Scanner sLists, Accounts[] users, Lists[] lists, int numusers) {

    if (sUsers.hasNextLine()) {
      String userFile = sUsers.nextLine();

      while (!userFile.equals("")) {

        String username = decode(userFile);
        userFile = shorten(userFile, username);

        String password = decode(userFile);
        userFile = shorten(userFile, password);

        String timeCreated = decode(userFile);
        userFile = shorten(userFile, timeCreated);

        users[numusers] = new Accounts(username, password, timeCreated);
        numusers++;
      }
    }

    if (sLists.hasNextLine()) {

      String listsFile = sLists.nextLine();

      for (int j = 0; j < numusers; j++) {

        String[] wishlist = new String[100];
        Accounts[] givers = new Accounts[100];
        String[] dates = new String[100];
        int numgifts = 0;

        getUsers: while (true) {

          String nextWord = decode(listsFile);
          listsFile = shorten(listsFile, nextWord);

          if (nextWord.equals("0")) {
            break getUsers;
          } else {
            wishlist[numgifts] = nextWord;

            String username = decode(listsFile);
            listsFile = shorten(listsFile, username);
            givers[numgifts] = Getters.getAccount(users, username);

            dates[numgifts] = decode(listsFile);
            listsFile = shorten(listsFile, dates[numgifts]);

            numgifts++;
          }
        }
        lists[j] = new Lists(wishlist, givers, dates);
      }
    }
    return numusers;
  }

  public static void printList(Accounts[] users, Lists[] lists, Accounts mainUser, Accounts user) {

    if (Getters.getNumEntries(users, lists, user) == 0) {
      if (mainUser == user)
        System.out.println("\nYou have no entries so far.");
      else
        System.out.println("\n" + user.username + " has no entries so far.");
    } else {

      System.out.println("\nList for " + user.username + ":\n");
      int num = Getters.getAccountNum(users, user);

      int maxWishlistLength = 0, maxUsernameLength = 0;
      for (int i = 0; i < Getters.getNumEntries(users, lists, user); i++) {
        if (lists[num].wishlist[i].length() > maxWishlistLength) {
          maxWishlistLength = lists[num].wishlist[i].length();
        }
        try {
          if (lists[num].givers[i].username.length() > maxUsernameLength) {
            maxUsernameLength = lists[num].givers[i].username.length();
          }
        } catch (NullPointerException e) {
        }
      }

      for (int i = 0; i < Getters.getNumEntries(users, lists, user); i++) {
        System.out.format("%d. %-" + (maxWishlistLength + 10) + "s", (i + 1), lists[num].wishlist[i]);
        if (!mainUser.username.equals(user.username) && lists[num].givers[i] != null) {
          System.out.format("%-" + (maxUsernameLength + 10) + "s", lists[num].givers[i].username);
        } else {
          System.out.format("%-" + (maxUsernameLength + 10) + "s", "");
        }
        System.out.println(lists[num].dates[i]);
      }
    }
  }

  public static void printFriends(Accounts[] users) {

    System.out.println();

    for (int i = 0; i < Getters.getNumUsers(users); i++) {
      System.out.println((i + 1) + ". " + users[i].username);
    }

  }
}