/**
 * Project: TextBased
 * File: GiftApp.java
 */

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * GiftApp! See README.md for description.
 * (Patent pending)
 * 
 * @author Jonathan Lane-Smith
 */
public class GiftApp {

  public static void entryScreen(Accounts[] users, Lists[] lists) {

    System.out.println("Welcome to GiftApp! Would you like to:");
    System.out.println("1. Login to your account.");
    System.out.println("2. Sign up for a new account.");
    System.out.println("3. Exit.");

    int input = Getters.getInput(1, 3);

    Accounts mainUser = null;

    if (input == 1) {
      mainUser = login(users);
    } else if (input == 2) {
      mainUser = signUp(users, lists);
    }

    if (input != 3 && mainUser == null) {
      System.out.println();
      entryScreen(users, lists);
    } else if (input != 3) {
      passwordProtector();
      menu(users, lists, mainUser);
    }
  }

  public static boolean verifyAccount(Accounts[] users, Accounts mainUser) {

    while (true) {
      System.out.println("\nEnter your current password.");
      String oldPassword = TextIO.getlnString();
      if (oldPassword.equals(mainUser.password)) {
        return true;
      } else {
        System.out.println("\nIncorrect.\n");
        System.out.println("1. Try again.");
        System.out.println("2. Go back.");

        int input = Getters.getInput(1, 2);
        if (input == 2)
          return false;
      }
    }
  }

  public static void changePassword(Accounts[] users, Lists[] lists, Accounts mainUser) {

    if (verifyAccount(users, mainUser)) {

      int num = Getters.getAccountNum(users, mainUser);
      System.out.println("\nEnter new password.");
      String newPassword = TextIO.getlnString();

      users[num].password = newPassword;
      passwordProtector();
    }
    menu(users, lists, mainUser);
  }

  public static void changeUsername(Accounts[] users, Lists[] lists, Accounts mainUser) {
    
    if (verifyAccount(users, mainUser)) {

      int num = Getters.getAccountNum(users, mainUser);
    
      label: while (true) {

        System.out.println("\nEnter new username.");
        String newUsername = TextIO.getlnString();

        if (Getters.getAccount(users, newUsername) == null) {
          users[num].username = newUsername;
          break label;
        } else
          System.out.println("Username already exists.");
      }
    }
    menu(users, lists, mainUser);
  }

  public static void deleteAccount(Accounts[] users, Lists[] lists, Accounts mainUser) {

    System.out.println("\nAre you sure? This action cannot be undone.");
    System.out.println("1. Yes, delete account.");
    System.out.println("2. Go back.");

    int input = Getters.getInput(1, 2);

    if (input == 2) {
      menu(users, lists, mainUser);
    } else {

      int accountNum = Getters.getAccountNum(users, mainUser);
      int numUsers = Getters.getNumUsers(users);
      users[accountNum] = null;
      lists[accountNum] = null;

      for (int i = accountNum; i < numUsers; i++) {
        if (users[i] != null || i == accountNum) {
          users[i] = users[i + 1];
          lists[i] = lists[i + 1];
        }
      }

      numUsers = Getters.getNumUsers(users);

      for (int i = 0; i < numUsers; i++) {

        int numEntries = Getters.getNumEntries(users, lists, users[i]);
        for (int j = 0; j < numEntries; j++) {
          if (lists[i].givers[j] == mainUser) {
            lists[i].givers[j] = null;
          }
        }
      }

      System.out.println();
      entryScreen(users, lists);
    }
  }

  public static void accountSettings(Accounts[] users, Lists[] lists, Accounts mainUser) {

    System.out.println("\nUsername: " + mainUser.username);
    System.out.print("Password: ");
    for (int i = 0; i < mainUser.password.length(); i++) {
      System.out.print("*");
    }
    System.out.println("\nAccount created: " + mainUser.timeAccountCreated);
    System.out.println("\n1. Change username.");
    System.out.println("2. Change password.");
    System.out.println("3. Delete account.");
    System.out.println("4. Go back.");

    int input = Getters.getInput(1, 4);

    if (input == 1) {
      changeUsername(users, lists, mainUser);
    } else if (input == 2) {
      changePassword(users, lists, mainUser);
    } else if (input == 3) {
      deleteAccount(users, lists, mainUser);
    } else
      menu(users, lists, mainUser);
  }

  public static Accounts login(Accounts[] users) {

    while (true) {

      System.out.println();
      System.out.println("Enter your username:");
      String username = TextIO.getlnString();
      System.out.println("Enter your password:");
      String password = TextIO.getlnString();

      Accounts person = Getters.getAccount(users, username, password);

      if (person != null)
        return person;
      else {
        System.out.println("\nIncorrect username and password");
        System.out.println("\n1. Try again.");
        System.out.println("2. Go back.");

        int input = Getters.getInput(1, 2);

        if (input == 2) {
          return null;
        }
      }
    }
  }

  public static Accounts signUp(Accounts[] users, Lists[] lists) {

    String username, password;

    label: while (true) {

      System.out.println("\nEnter a username:");
      username = TextIO.getlnString();

      if (Getters.getAccount(users, username) == null)
        break label;
      else {

        System.out.println("\nThat username already exists.");
        System.out.println("\n1. Try again.");
        System.out.println("2. Go back.");

        int input = Getters.getInput(1, 2);

        if (input == 2) {
          return null;
        }

      }
    }

    System.out.println("Enter a password:");
    password = TextIO.getlnString();

    int newNum = Getters.getNumUsers(users);
    users[newNum] = new Accounts(username, password);
    
    String[] wishlist = new String[100];
    Accounts[] givers = new Accounts[100];
    String[] dates = new String[100];
    lists[newNum] = new Lists(wishlist, givers, dates);
    return users[newNum];
  }

  public static void menu(Accounts[] users, Lists[] lists, Accounts mainUser) {

    System.out.println("\n1. View your list.");
    System.out.println("2. View users.");
    System.out.println("3. Account settings.");
    System.out.println("4. Logout.");

    int input = Getters.getInput(1, 4);

    if (input == 1) {
      selfList(users, lists, mainUser, true);
    } else if (input == 2) {
      chooseFriend(users, lists, mainUser);
    } else if (input == 3) {
      accountSettings(users, lists, mainUser);
    } else {
      System.out.println();
      entryScreen(users, lists);
    }
  }

  public static void selfList(Accounts[] users, Lists[] lists, Accounts mainUser, boolean backToMain) {

    InputOutput.printList(users, lists, mainUser, mainUser);

    System.out.println("\n1. Add an entry.");
    System.out.println("2. Delete an entry.");
    System.out.println("3. Go back.");

    int input = Getters.getInput(1, 3);

    if (input == 1) {
      addEntry(users, lists, mainUser, backToMain);
    } else if (input == 2) {
      deleteEntry(users, lists, mainUser, backToMain);
    } else if (backToMain) {
      menu(users, lists, mainUser);
    } else {
      chooseFriend(users, lists, mainUser);
    }
  }

  public static void addEntry(Accounts[] users, Lists[] lists, Accounts mainUser, boolean backToMain) {

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
    Date date = new Date();

    System.out.println("\nWhat would you like to add?");
    String gift = TextIO.getlnString() + " ";

    int currentEntries = Getters.getNumEntries(users, lists, mainUser);
    int accountNum = Getters.getAccountNum(users, mainUser);
    lists[accountNum].wishlist[currentEntries] = gift;
    lists[accountNum].dates[currentEntries] = dateFormat.format(date);

    selfList(users, lists, mainUser, backToMain);
  }

  public static void deleteEntry(Accounts[] users, Lists[] lists, Accounts mainUser, boolean backToMain) {

    int currentEntries = Getters.getNumEntries(users, lists, mainUser);

    if (currentEntries > 0) {

      System.out.println("\nWhich entry would you like to delete?");
      int input = Getters.getInput(1, currentEntries);
      int accountNum = Getters.getAccountNum(users, mainUser);

      lists[accountNum].wishlist[input - 1] = null;
      lists[accountNum].givers[input - 1] = null;
      lists[accountNum].dates[input - 1] = null;

      for (int i = input - 1; i < currentEntries; i++) {
        if (lists[accountNum].wishlist[i] != null || i == input - 1) {
          lists[accountNum].wishlist[i] = lists[accountNum].wishlist[i + 1];
          lists[accountNum].givers[i] = lists[accountNum].givers[i + 1];
          lists[accountNum].dates[i] = lists[accountNum].dates[i + 1];
        }
      }
    }
    selfList(users, lists, mainUser, backToMain);
  }


  public static void chooseFriend(Accounts[] users, Lists[] lists, Accounts mainUser) {

    int currentUsers = Getters.getNumUsers(users);

    InputOutput.printFriends(users);
    System.out.println("\n" + (currentUsers + 1) + ". Go back.");

    System.out.println("\nWhose list would you like to look at? (Enter number)");

    int input = Getters.getInput(1, currentUsers + 1);

    if (input == currentUsers + 1) {
      menu(users, lists, mainUser);
    } else if (users[input - 1] == mainUser) {
      selfList(users, lists, mainUser, false);
    } else {
      friendList(users, lists, mainUser, users[input - 1]);
    }
  }

  public static void friendList(Accounts[] users, Lists[] lists, Accounts mainUser, Accounts friend) {

    InputOutput.printList(users, lists, mainUser, friend);

    if (Getters.getNumEntries(users, lists, friend) == 0) {

        System.out.println("1. Go back.");
        int choice = Getters.getInput(1, 1);
        if (choice == 1)
          menu(users, lists, mainUser);

      } else {

        System.out.println("\n1. Add your name to an entry.");
        System.out.println("2. Remove your name from an entry.");
        System.out.println("3. Go back.");

        int option = Getters.getInput(1, 3);

        if (option == 1)
        addName(users, lists, mainUser, friend);
        else if (option == 2)
        removeName(users, lists, mainUser, friend);
        else
        chooseFriend(users, lists, mainUser);
    }
  }

  public static void addName(Accounts[] users, Lists[] lists, Accounts mainUser, Accounts friend) {

    int currentEntries = Getters.getNumEntries(users, lists, friend);
    int numFriendAccount = Getters.getAccountNum(users, friend);

    System.out.println("\nWhich entry would you like to add your name to?");

    int input = Getters.getInput(1, currentEntries);

    if (lists[numFriendAccount].givers[input - 1] == null) {
      lists[numFriendAccount].givers[input - 1] = mainUser;
    } else {
      System.out.println("Sorry, this is already taken.");
    }

    friendList(users, lists, mainUser, friend);
  }

  public static void removeName(Accounts[] users, Lists[] lists, Accounts mainUser, Accounts friend) {

    int currentEntries = Getters.getNumEntries(users, lists, friend);

    int numFriendAccount = Getters.getAccountNum(users, friend);
    System.out.println("\nWhich entry would you like to remove your name from?");

    int input = Getters.getInput(1, currentEntries);
    if (lists[numFriendAccount].givers[input - 1] == mainUser) {
      lists[numFriendAccount].givers[input - 1] = null;
    } else {
      System.out.println("Sorry, you can't do that.");
    }

    friendList(users, lists, mainUser, friend);
  }

  public static void passwordProtector() {

    System.out.println();

    for (int i = 0; i < 10; i++) {
      System.out.println("*\n *\n  *\n  *\n *\n*");
    }
  }

  public static void main(String[] args) throws IOException {

    File inUsers = new File("users.txt");
    File inLists = new File("lists.txt");
    Scanner sUsers = new Scanner(inUsers);
    Scanner sLists = new Scanner(inLists);

    Accounts[] users = new Accounts[100];
    Lists[] lists = new Lists[100];
    InputOutput.readFiles(sUsers, sLists, users, lists, Getters.getNumUsers(users));
    sUsers.close();
    sLists.close();

    entryScreen(users, lists);

    InputOutput.writeFiles(users, lists, Getters.getNumUsers(users));
  }
}