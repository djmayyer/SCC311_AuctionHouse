package api;

import java.util.*;
import shared.AuctionItem;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface API extends Remote {
  
  public AuctionItem addAuctionItem(int itemId, int clientId, String itemTitle, String itemDescription, double itemPrice, double itemReservePrice) throws RemoteException;

  public AuctionItem getSpec(int itemId, int clientId) throws RemoteException;

  public void bidAuctionItem(int itemId, int clientId, double newBid, String bidNameWinner, String bidEmailWinner) throws RemoteException;

  public boolean modifyReservePrice(int itemId, int clientId, double newRevPrice) throws RemoteException;

  public boolean modifyDescription(int itemId, int clientId, String newDescription) throws RemoteException;

  public boolean modifyName(int itemId, int clientId, String newName) throws RemoteException;

  public boolean closeAuctionItem(int itemId, int clientId) throws RemoteException;

  public int countAuctionItem() throws RemoteException;
}
