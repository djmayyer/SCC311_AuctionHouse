package frontend;

import org.jgroups.JChannel;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

import api.API;
import backend.Backend;
import shared.AuctionItem;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import utility.GroupUtils;
import shared.AuctionItem;

public class Frontend extends UnicastRemoteObject implements API {

  public static final long serialVersionUID = 42069;

  public final String SERVER_NAME = "random_number_generator";
  public final int REGISTRY_PORT = 1099;

  private JChannel groupChannel;
  private RpcDispatcher dispatcher;

  private final int DISPATCHER_TIMEOUT = 1000;

  public Frontend() throws RemoteException {
    // Connect to the group (channel)
    this.groupChannel = GroupUtils.connect();
    if (this.groupChannel == null) {
      System.exit(1); // error to be printed by the 'connect' function
    }

    // Bind this server instance to the RMI Registry
    this.bind(this.SERVER_NAME);

    // Make this instance of Frontend a dispatcher in the channel (group)
    this.dispatcher = new RpcDispatcher(this.groupChannel, this);
    this.dispatcher.setMembershipListener(new MembershipListener());

  }

  private void bind(String serverName) {
    try {
      Registry registry = LocateRegistry.createRegistry(this.REGISTRY_PORT);
      registry.rebind(serverName, this);
      System.out.println("✅    rmi server running...");
    } catch (Exception e) {
      System.err.println("🆘    exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public AuctionItem addAuctionItem(int itemId, int clientId, String itemTitle, String itemDescription, double itemPrice, double itemReservePrice) throws RemoteException
  {
    System.out.printf("📩    adding an Auction\n🧮\n");
    try{
      RspList<AuctionItem> response_addAuctionItem = this.dispatcher.callRemoteMethods(null, "addAuctionItem",new Object[] {itemId, clientId, itemTitle, itemDescription, itemPrice, itemReservePrice}, new Class[] {int.class, int.class, String.class, String.class, double.class, double.class}, new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_addAuctionItem.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return null;
  }

  public AuctionItem getSpec(int itemId, int clientId) throws RemoteException 
  {
    System.out.printf("📩     getting Specs\n🧮\n");
    try{
      RspList<AuctionItem> response_getSpec = this.dispatcher.callRemoteMethods(null, "getSpec",new Object[] {itemId, clientId}, new Class[] {int.class, int.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_getSpec.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return null;
  }

  public void bidAuctionItem(int itemId, int clientId, double newBid, String bidNameWinner, String bidEmailWinner) throws RemoteException
  {
    System.out.printf("📩    Bidding on Auction Items\n🧮\n");
    try{
      RspList<Integer> response_bidAuctionItem = this.dispatcher.callRemoteMethods(null, "bidAuctionItem",new Object[] {itemId, clientId, newBid, bidNameWinner, bidEmailWinner}, new Class[] {int.class, int.class, double.class, String.class, String.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      //return response_bidAuctionItem.getFirst();
      System.out.println("blablkabla");
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
 
  }

  public boolean modifyReservePrice(int itemId, int clientId, double newRevPrice) throws RemoteException
  {
    System.out.printf("📩    modify Reserve Price\n🧮\n");
    try{
      RspList<Boolean> response_modifyReservePrice = this.dispatcher.callRemoteMethods(null, "modifyReservePrice",new Object[] {itemId, clientId, newRevPrice}, new Class[] {int.class, int.class, double.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_modifyReservePrice.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return false;
  }

  public boolean modifyDescription(int itemId, int clientId, String newDescription) throws RemoteException
  {
    System.out.printf("📩    Modify Description\n🧮\n");
    try{
      RspList<Boolean> response_modifyDescription= this.dispatcher.callRemoteMethods(null, "modifyDescription",new Object[] {itemId, clientId, newDescription}, new Class[] {int.class, int.class, String.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_modifyDescription.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean modifyName(int itemId, int clientId, String newName) throws RemoteException
  {
    System.out.printf("📩    modify Name\n🧮\n");
    try{
      RspList<Boolean> response_modifyName = this.dispatcher.callRemoteMethods(null, "modifyName",new Object[] {itemId, clientId, newName}, new Class[] {int.class, int.class, String.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_modifyName.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return false;
  }

  public boolean closeAuctionItem(int itemId, int clientId) throws RemoteException
  {
    System.out.printf("📩    close Auction Item\n🧮\n");
    try{
      RspList<Boolean> response_closeAuctionItem = this.dispatcher.callRemoteMethods(null, "modifyName",new Object[] {itemId, clientId}, new Class[] {int.class, int.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_closeAuctionItem.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
    }
    return false;
  }

  public int countAuctionItem() throws RemoteException
  {
    System.out.printf("📩    counting Auction Item\n🧮 \n");
    try{
      RspList<Integer> response_countAuctionItem= this.dispatcher.callRemoteMethods(null, "countAuctionItem",new Object[] { }, new Class[] { },new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
      return response_countAuctionItem.getFirst();
    } catch (Exception e){
      System.err.println("🆘    dispatcher exception:");
      e.printStackTrace();
  }
  return -1;
}
  public static void main(String args[]) {
    try {
      new Frontend();
    } catch (RemoteException e) {
      System.err.println("🆘    remote exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }

}
