package me.kishso.autosort;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class AutoSortBook {

    ArrayList<Material> orderList = new ArrayList<>();
    ItemStack book = null;

    //Constructor
    public AutoSortBook(ItemStack book){
        if(book.getItemMeta() instanceof BookMeta){
            BookMeta meta = (BookMeta) book.getItemMeta();
            //Set Author to Plugin Name
            if(!meta.getAuthor().equals("AutoSort Plugin")) {
                meta.setAuthor("AutoSort Plugin");
                book.setItemMeta(meta);
            }
            //Retrieve Order from Book Pages
            for(int page = 1; page <= meta.getPageCount() && page <= 54; page++){
                if(!meta.getPage(page).isEmpty()){
                    orderList.add(Material.valueOf(meta.getPage(page)));
                }
            }
            //Set Book to Class Variable
            this.book = book.clone();
        }
    }

    public Inventory getInventoryOrder(){
        Inventory inv = Bukkit.createInventory(null,54,"AutoSort");
        for(int i = 0; i < orderList.size(); i++){
            if(orderList.get(i) != null){
                inv.setItem(i, new ItemStack(orderList.get(i),1));
            }
        }
        return inv;
    }

    public void setInventoryOrder(Inventory inv){
        orderList.clear();
        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR){
                if(!orderList.contains(inv.getItem(i).getType())) {
                    orderList.add(inv.getItem(i).getType());
                }
            }
        }
    }

    public ItemStack writeToBook(){
        if(book.getItemMeta() instanceof BookMeta){
            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.setPages(new String[54]);
            for(int i = 0; i < orderList.size(); i++){
                if(orderList.get(i) != null){
                    meta.setPage(i + 1, orderList.get(i).toString());
                }
            }
            book.setItemMeta(meta);
        }
        return book;
    }

    public ItemStack[] sort(ItemStack[] contents){
            for(int i = 0; i < contents.length - 1; i++){
                    int lowest = i;
                    for (int j = i + 1; j < contents.length - 1; j++) {
                        if (contents[j] != null) {
                            if(contents[lowest] != null) {
                                //Case Both Are Defined
                                if (orderList.contains(contents[lowest].getType()) && orderList.contains(contents[j].getType())) {
                                    //Case i is defined first
                                        //lowest = lowest
                                    //Case j is defined first
                                    if (orderList.indexOf(contents[j].getType()) < orderList.indexOf(contents[lowest].getType())) {
                                        lowest = j;
                                    }
                                    //Case i and j are the same material
                                    else if (contents[lowest].isSimilar(contents[j])) {
                                        int total = contents[lowest].getAmount() + contents[j].getAmount();
                                        //Case total is less than or equal to max stack size
                                        if (total <= contents[lowest].getMaxStackSize()) {
                                            contents[lowest].setAmount(total);
                                            contents[j] = null;
                                        }
                                        //Case total is higher than max stack size
                                        else {
                                            contents[lowest].setAmount(contents[lowest].getMaxStackSize());
                                            contents[j].setAmount(total - contents[lowest].getMaxStackSize());
                                        }
                                    }
                                }
                                //Case i is defined
                                    //lowest = lowest
                                //Case j is defined
                                else if(orderList.contains(contents[j].getType())){
                                    lowest = j;
                                }
                            }else{
                                lowest = j;
                            }
                        }
                    }//End of Inner Loop
                    ItemStack temp = contents[i];
                    contents[i] = contents[lowest];
                    contents[lowest] = temp;
            }//End of Outer Loop
        return contents;
    }


    
}
