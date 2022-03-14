package shared;

import java.io.Serializable;
import java.util.*;
import java.io.*;

public class AuctionItem implements Serializable
{
    //Item features
    public int itemId;
    public String itemTitle;
    public String itemDescription;

    //Bets
    public double currBidPrice;
    public double reservePrice;

    //IDs
    public double aucClientId;
    public int clientId;

    //Buyer Details
    public String nameWinner;
    public String emailWinner;
}