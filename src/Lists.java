/**
 * Project: TextBased
 * File: Lists.java
 */

/**
 * @author Jonathan Lane-Smith
 */
public class Lists {

  public String[] wishlist;
  public Accounts[] givers;
  public String[] dates;

  Lists(String[] wishlist, Accounts[] givers, String[] dates) {
    this.wishlist = wishlist;
    this.givers = givers;
    this.dates = dates;
  }
}