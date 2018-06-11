
package steamitemmonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class realSteamMonitor extends TimerTask {
    Date currentTime;
    static ArrayList<Integer> itemQuantitysPrevious = new ArrayList<>();
    static ArrayList<String> itemNamesPrevious = new ArrayList<>();
    
    @Override
    public void run() {
        currentTime = new Date();
        String siteLink = "https://steamcommunity.com/market/search?appid=730";
        int itemQuantity = 0;
        String itemName = "";
        int count = 0, rCount = 0;
        //new array
        ArrayList<Integer> itemQuantitys = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();
        Element listing;

        Document dc;
        try {
            dc = Jsoup.connect(siteLink).timeout(6000).get();
        
        Element topItemTable = dc.getElementById("searchResultsRows");
        Elements topItems = topItemTable.getElementsByClass("market_listing_row market_recent_listing_row market_listing_searchresult");
        
                System.out.println("--------- New Scan Occurring Of Site: " + siteLink + " At --> " + currentTime + " ---------\n\n");
                itemQuantitys.clear();
                itemNames.clear();
                count = 0;
                
                //grab all items names and quantities and put them in array
                for (Element item : topItems){
                        listing = item.select("div.market_listing_price_listings_block").first();
                        itemQuantity = Integer.parseInt(listing.select("span.market_listing_num_listings_qty").attr("data-qty"));
                        itemName = item.select("span.market_listing_item_name").text();
                        itemQuantitys.add(itemQuantity);
                        itemNames.add(itemName);
                        if(itemQuantitysPrevious.size() < itemQuantitys.size()) {
                            itemQuantitysPrevious.add(itemQuantity);
                            itemNamesPrevious.add(itemName);
                        }

                    }
                
                for (int quantity : itemQuantitys) {
                    for(int j = 0; j < itemNames.size(); j++) {
                        if(itemNames.get(j).equals(itemNamesPrevious.get(count))) {
                            rCount = j;
                        }
                    }
                    if(quantity > itemQuantitysPrevious.get(rCount)) {
                        System.out.println("- CHANGE FOUND - ");
                        System.out.println("ITEM: " + itemNamesPrevious.get(rCount));
                        System.out.println("INCREASE From: " + itemQuantitysPrevious.get(rCount) + " TO: " + quantity + "\n");
                    } else if(quantity < itemQuantitysPrevious.get(rCount)) {
                        System.out.println("- CHANGE FOUND - ");
                        System.out.println("ITEM: " + itemNamesPrevious.get(rCount));
                        System.out.println("DECREASE From: " + itemQuantitysPrevious.get(rCount) + " TO: " + quantity + "\n");
                       
                    }
                    count++;
                }
                } catch (IOException ex) {
            Logger.getLogger(realSteamMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
