package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import api.API;
import shared.AuctionItem;
import java.util.*;
import java.io.*;
import javax.lang.model.util.ElementScanner14;

public class Client {

  private final String SERVER_NAME = "random_number_generator";
  public final int REGISTRY_PORT = 1099;

  public Client(int n) {
    try {
      Registry registry = LocateRegistry.getRegistry();
      API server = (API) registry.lookup(this.SERVER_NAME);
      
      /**
      *Buyer and Seller Selection
      */
      boolean menuType = true;
      boolean userBuyer = false;
      boolean userSeller = false;
      while(menuType)
      {
        System.out.print("Please select:\n->buy\n->sell\n");
        Scanner inputUserType = new Scanner(System.in);
        String inputUser = inputUserType.nextLine();
        //check the user input
        if(inputUser.equals("buy")|| inputUser.equals("sell"))
        {
          menuType = false;
          System.out.println("-------------------------");
          System.out.print("You are "+inputUser+"\n");
          if(inputUser.equals("buy")) userBuyer = true;
          if(inputUser.equals("sell")) userSeller = true;
        }
        else{
          System.out.println("Please enter a valid user type");
        }
      }

      /**
      * Seller Menu
      */
      if(userSeller)
      { 
        System.out.println("-------------------------");
        System.out.println("User ID = "+n);
        boolean menu = true;
        while(menu)
        {
          System.out.println("-------------------------");
          System.out.println("Please select:");
          System.out.println("->list\n->add\n->modify\n->close\n->exit");
          System.out.println("-------------------------");
          Scanner myInputMenu =  new Scanner(System.in);
          String inputMenu = myInputMenu.nextLine();
          
          //EXIT Buyer Menu
          if(inputMenu.equals("exit")) menu = false;

          //LIST AUCTION ITEM
          if(inputMenu.equals("list"))
          {
            //List how many Items are available.
            int outputNoAuctionItem = server.countAuctionItem();
            if(outputNoAuctionItem > 0)
            {
              System.out.println("There are "+outputNoAuctionItem+" auctioned items available");
              //List Auction Item
              System.out.println("Please enter itemID you wish to list: ");
              Scanner myInput = new Scanner(System.in);
              int inputItemId = myInput.nextInt();

              //Check if the input value is valid regarding the available items
              if(inputItemId>outputNoAuctionItem|| inputItemId == 0)
              {
                System.out.println("Please enter a valid Item, there are only "+outputNoAuctionItem+" Auction Items available");
              }
              else
              {
                AuctionItem item1 = server.getSpec(inputItemId-1, n);
                System.out.println("--------item Id:" + inputItemId+ "--------");
                System.out.println("item Title = " + item1.itemTitle);
                System.out.println("item Description = " + item1.itemDescription);
                System.out.println("item Price = "+item1.currBidPrice);
                System.out.println("item userId = "+item1.clientId);
                System.out.println("-------------------------");
              }
            }
            else{
              System.out.println("There are no items listed");
            }
          }

          //CLOSE AUCTION 
          if(inputMenu.equals("close"))
          {
            boolean closeMenu = true;
            //list user items
            int removeAvailableItems = server.countAuctionItem();
            System.out.println("There are "+removeAvailableItems+" Auctions Available to close");

            //close an auction
            while(closeMenu)
            {
              System.out.println("Please enter the itemId you want to remove");
              Scanner myInputremove = new Scanner(System.in);
              int inputRemove = myInputremove.nextInt();
              //double inputRemove = Double.parseDouble(myInputremove.next());
              AuctionItem closeitem1 = server.getSpec(inputRemove-1, n);
              if(removeAvailableItems >= inputRemove)
              {
                if(server.closeAuctionItem(inputRemove-1,n))
                {
                  System.out.print("Auction "+inputRemove+" was closed successfully\n");
                  if(closeitem1.currBidPrice >= closeitem1.reservePrice)
                  {
                    System.out.println("------Winner Details:------");
                    String ANSI_YELLOW = "\u001B[33m";
                    String ANSI_RESET = "\u001B[0m";
                    System.out.println("name:"+ANSI_YELLOW+ closeitem1.nameWinner+ANSI_RESET+"\nemail:"+ANSI_YELLOW+closeitem1.emailWinner+ANSI_RESET);
                    System.out.println("---------------------------");
                  }
                  else
                  {
                    String ANSI_RED = "\u001B[31m";
                    String ANSI_RESET = "\u001B[0m";
                    System.out.println(ANSI_RED + "The reserved price wasn`t riched and there is no winner"+ANSI_RESET);
                  }
                  closeMenu = false;
                }
                else
                {
                  System.out.print("You cannot close this Auction because you are not the owner of this Item\n");
                  closeMenu = false;
                }
              }
              else
              {
                System.out.println("The number you have entered is not valid");
              }
            }
          }

          //MODIFY AUCTION ITEM
          if(inputMenu.equals("modify"))
          {
            Boolean modifyMenu = true;
            System.out.println("Please enter ItemID");
            Scanner myInputModify = new Scanner(System.in);
            int inputModify = myInputModify.nextInt();
            
            while(modifyMenu)
            {
              System.out.println("-------------ItemID:"+inputModify+"----------------");
              System.out.println("Please select what you want to modify\n->name\n->description\n->reserve\n->close");
              System.out.println("-------------------------------------");
              String inputModifyElement = myInputModify.nextLine();
              if(inputModifyElement.equals("name"))
              {
                System.out.println("Please enter a new name:");
                String inputModifyName = myInputModify.nextLine();
                if(server.modifyName(inputModify-1,n,inputModifyName))
                {
                  System.out.println("");
                }
              }
              if(inputModifyElement.equals("description"))
              {
                System.out.println("Please enter a new description:");
                String inputModifyDescription = myInputModify.nextLine();
                server.modifyDescription(inputModify-1,n,inputModifyDescription);
              }
              if(inputModifyElement.equals("reserve"))
              {
                System.out.println("Please enter a new reserve price:");
                double inputModifyReserve = Double.parseDouble(myInputModify.next());
                server.modifyReservePrice(inputModify-1,n,inputModifyReserve);
              }
              if(inputModifyElement.equals("close"))
              {
                modifyMenu = false;
              }
            }
          }
          
          //ADD AUCTION ITEM
          if(inputMenu.equals("add"))
          {
            Boolean addMenu = true;
            // AuctionItem item1 = server.getSpec(inputItemId, 7);

            System.out.println("Add an Auction item:");

            // Adding Item Title
            System.out.println("Item Title: ");
            Scanner myInputString = new Scanner(System.in);
            String inputItemTitle = myInputString.nextLine();

            // Adding Itme Description
            System.out.println("Item Description:");
            String inputItemDescription = myInputString.nextLine();

            // Adding Starting Price
            System.out.println("Starting Price:");
            Scanner myInputFloat = new Scanner(System.in);
            double inputStartingPrice = Double.parseDouble(myInputFloat.next());
            
            while(addMenu)
            {
              // Adding a Reserve Price
              System.out.println("Reserve Price:");
              double inputReservePrice = Double.parseDouble(myInputFloat.next());
              if(inputReservePrice > inputStartingPrice)
              {
                addMenu = false;
                // Adding the object into the arraylist
                AuctionItem item2 = server.addAuctionItem(1, n,inputItemTitle,inputItemDescription,inputStartingPrice,inputReservePrice);
                System.out.println("You have successfully added an item with id: "+ item2.itemId);
              }
              else{
                System.out.println("Please enter a greater value than the Starting Price");
              }
            }
          }
        }
      }

      /**
      * Buyer Menu
      */
      if(userBuyer)
      {
        System.out.println("-------------------------");
        System.out.println("User ID = "+n);
        boolean menu = true;

        //Seller Menu
        while(menu)
        {
          System.out.println("-------------------------");
          System.out.println("Please select:");
          System.out.println("->list\n->bid\n->exit");
          System.out.println("-------------------------");
          Scanner myInputMenu =  new Scanner(System.in);
          String inputMenu = myInputMenu.nextLine();

          //EXIT Seller Menu
          if(inputMenu.equals("exit")) menu = false;

          //BID AUCTION ITEM
          if(inputMenu.equals("bid"))
          {
            //User Details
            Scanner inputBidUserName = new Scanner(System.in);
            Scanner inputBidUserEmail = new Scanner(System.in);
            System.out.println("Enter your details");
            System.out.print("name: ");
            String inputUserName = inputBidUserName.nextLine();
            System.out.print("email: ");
            String inputUserEmail = inputBidUserEmail.nextLine();
            
            boolean bidMenu = true;
            int outputNoAuctionItem = server.countAuctionItem();
            System.out.println("There are "+outputNoAuctionItem+" auctioned items available");
            System.out.println("Please select which item you want to bid for");
            Scanner myInput = new Scanner(System.in);
            int inputItemId = myInput.nextInt();

            if(inputItemId-1<outputNoAuctionItem)
            {
              AuctionItem item1 = server.getSpec(inputItemId-1, n);
              //item1.itemId = inputItemId;
              System.out.println("--------item Id:" + item1.itemId + "--------");
              System.out.println("item Title = " + item1.itemTitle);
              System.out.println("item Description = " + item1.itemDescription);
              System.out.println("item Price = "+item1.currBidPrice);
              System.out.println("item userId = "+item1.clientId);
              System.out.println("-------------------------");

              while(bidMenu)
              { 
                System.out.println("Enter how much you want to bid");
                double inputBidPrice = Double.parseDouble(myInput.next());
              
                if(inputBidPrice > item1.currBidPrice)
                {
                  server.bidAuctionItem(inputItemId-1,n,inputBidPrice,inputUserName,inputUserEmail);
                  System.out.println("You bid "+ inputBidPrice +" for Item "+inputItemId);
                  bidMenu = false;
                }
                else{
                  System.out.println("Please enter a value grater than the current Bid "+item1.currBidPrice);
                }
              }
            }
            else
            {
              System.out.println("You have entered a greater value the the number of listed items");
            }
          }
          //LIST AUCTION ITEM
          if(inputMenu.equals("list"))
          {
            //List how many Items are available.
            int outputNoAuctionItem = server.countAuctionItem();
            System.out.println("There are "+outputNoAuctionItem+" auctioned items available");
            //List Auction Item
            System.out.println("Please enter itemID you wish to list: ");
            Scanner myInput = new Scanner(System.in);
            int inputItemId = myInput.nextInt();

            //Check if the input value is valid regarding the available items
            if(inputItemId>outputNoAuctionItem|| inputItemId == 0)
            {
              System.out.println("Please enter a valid Item, there are only "+outputNoAuctionItem+" Auction Items available");
            }
            else
            {
              AuctionItem item1 = server.getSpec(inputItemId-1, 7);
              item1.itemId = inputItemId;
              System.out.println("--------item Id:" + item1.itemId + "--------");
              System.out.println("item Title = " + item1.itemTitle);
              System.out.println("item Description = " + item1.itemDescription);
              System.out.println("item Price = "+item1.currBidPrice);
              System.out.println("item userId = "+item1.clientId);
              System.out.println("-------------------------");
            }
          }
        }
      }
    } catch (Exception e) {
      System.err.println("🆘 exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void main(String[] args) {

    if (args.length < 1){
      System.out.printf("🚨 error: must supply min and mac values for the random number\n\tusage | <command> min(int) max(int)");
      System.exit(1);
    }
    
    int n = Integer.parseInt(args[0]);
    new Client(n);

    //CLIENT PART B
    //new Client(n);
  }
}