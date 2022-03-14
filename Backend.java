package backend;

import org.jgroups.JChannel;
import org.jgroups.blocks.RpcDispatcher;
import frontend.Frontend;
import java.util.Random;
import utility.GroupUtils;
import shared.AuctionItem;
import java.util.*;

public class Backend {

  private JChannel groupChannel;
  private RpcDispatcher dispatcher;
  private int requestCount;

  ArrayList<AuctionItem> list = new ArrayList<AuctionItem>();

  public Backend() {
    this.requestCount = 0;

    // Connect to the group (channel)
    this.groupChannel = GroupUtils.connect();
    if (this.groupChannel == null) {
      System.exit(1); // error to be printed by the 'connect' function
    }

    // Make this instance of Backend a dispatcher in the channel (group)
    this.dispatcher = new RpcDispatcher(this.groupChannel, this);
  }

  public int countAuctionItem()
   {
      System.out.println("Client wants to list an Item");
      return list.size();
   }

  public boolean closeAuctionItem(int itemId, int clientId)
   {
      if(clientId == list.get(itemId).clientId)
      {
         System.out.println("client requested to close an auction");
         list.remove(itemId);
         return true;
      }
      return false;
   }

  public boolean modifyName(int itemId, int clientId, String newName)
   {
      if(clientId == list.get(itemId).clientId)
      {
         System.out.println("Client "+clientId+" modified the Name of item "+itemId);
         list.get(itemId).itemTitle = newName;
         return true;
      }
      return false;
   }

  public boolean modifyDescription(int itemId, int clientId, String newDescription)
   {
      if(clientId == list.get(itemId).clientId)
      {
         System.out.println("Client "+clientId+" modified the Description of item "+itemId);
         list.get(itemId).itemDescription = newDescription;
         return true;
      }
      return false;
   }

  public boolean modifyReservePrice(int itemId, int clientId, double newRevPrice)
   {
      if(clientId == list.get(itemId).clientId)
      {
         System.out.println("Client "+clientId+" modified the Reverse Price of item "+itemId);
         list.get(itemId).reservePrice = newRevPrice;
         return true;
      }
      return false;
   }

  public void bidAuctionItem(int itemId, int clientId, double newBid, String bidNameWinner, String bidEmailWinner)
   {  
      list.get(itemId).currBidPrice = newBid;
      System.out.println("Client "+ clientId +" raised a new bet: "+ newBid);
      list.get(itemId).nameWinner = bidNameWinner;
      list.get(itemId).emailWinner = bidEmailWinner;
   }

  public AuctionItem addAuctionItem(int itemId, int clientId, String itemTitle, String itemDescription, double itemPrice, double itemReservePrice)
  {
    System.out.println("client requested to add an auction");
    // Creating an Auction Item object and adding its details
    AuctionItem item2 = new AuctionItem();
    item2.itemId = itemId;
    item2.itemTitle = itemTitle;
    item2.itemDescription = itemDescription;
    item2.aucClientId = clientId;
    item2.currBidPrice = itemPrice;
    item2.reservePrice = itemReservePrice;
    item2.clientId = clientId;

    // adding the object into an array list
    list.add(item2);
    item2.itemId = list.size();
    System.out.println("new item was added with Id: "+item2.itemId);
    return item2;
   }

   public AuctionItem getSpec(int itemId, int clientId)
   {
    System.out.println("client request handled");
    return list.get(itemId);
   }

  public static void main(String args[]) {
    new Backend();
  }

}
