package me.kishso.autosort;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.logging.Handler;

public class AutoSortEvents implements Listener {

    ArrayList<Inventory> openInvs = new ArrayList<>();
    ArrayList<AutoSortBook> openBooks = new ArrayList<>();

    @EventHandler
    public void openGUI(PlayerInteractEvent event){
        //Check Permission
        if(event.getPlayer().hasPermission("autosort.book.open")){
            //RightClick Air
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
                //If it is a Book
                if(event.hasItem() && event.getItem().getType().equals(Material.WRITTEN_BOOK)){
                    //Check Author
                    if(event.getItem().getItemMeta() instanceof BookMeta) {
                        BookMeta meta = (BookMeta) event.getItem().getItemMeta();
                        if(meta.getAuthor().equals("AutoSort Plugin")){
                            event.setCancelled(true);
                            AutoSortBook book = new AutoSortBook(event.getItem());
                            openBooks.add(book);
                            event.getItem().setAmount(0);
                            Inventory inv = book.getInventoryOrder();
                            event.getPlayer().openInventory(inv);
                            openInvs.add(inv);
                        }
                    }
                }

            }
        }else{
            System.out.println("No Permission");
        }
    }

    @EventHandler
    public void closeGUI(InventoryCloseEvent event){
        if(openInvs.contains(event.getInventory())){
            AutoSortBook book = openBooks.get(openInvs.indexOf(event.getInventory()));
            book.setInventoryOrder(event.getView().getTopInventory());
            event.getPlayer().getInventory().addItem(book.writeToBook());
            openBooks.remove(openInvs.indexOf(event.getInventory()));
            openInvs.remove(openInvs.indexOf(event.getInventory()));
        }
    }

    @EventHandler
    public void interactGUI(InventoryClickEvent event){
        if(openInvs.contains(event.getClickedInventory())){
            event.setCancelled(true);
            event.getClickedInventory().setItem(event.getRawSlot(),new ItemStack(event.getCursor().getType(),1));
        }
    }

    @EventHandler
    public void sort(InventoryCloseEvent event){
        //Checks that the closed inventory is not an autosort gui
        if(!openInvs.contains(event.getInventory())) {
            //Copies array of contents from the closed inventory
            ItemStack[] contents = event.getInventory().getStorageContents();
            //Initialize local variables
            AutoSortBook book = null;
            boolean containsBook = false;
            //Loops through the inventory to find the first book that meets conditions
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null && contents[i].getItemMeta() instanceof BookMeta) {
                    BookMeta meta = (BookMeta) contents[i].getItemMeta();
                    if (meta.getAuthor().equals("AutoSort Plugin")) {
                        //Condition Met, swap the book to the last index of the array
                        book = new AutoSortBook(contents[i]);
                        ItemStack temp = contents[contents.length - 1];
                        contents[contents.length - 1] = contents[i];
                        contents[i] = temp;
                        //There exists an AutoSort Book
                        containsBook = true;
                    }
                }
            }
            //Check if there exists an AutoSort Book and sort based off that book.
            if (containsBook) {
                event.getInventory().setStorageContents(book.sort(contents));
                System.out.print("Chest Sorted");
            }
        }
    }
}
