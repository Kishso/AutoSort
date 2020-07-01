package me.kishso.easyopenshulkerboxes;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;


public class CustomShulkerEvents implements Listener {

    ArrayList<InventoryView> prevInv = new ArrayList<>();
    ArrayList<Inventory> openInvs = new ArrayList<>();
    ArrayList<Integer> openSlots = new ArrayList<>();
    ArrayList<ItemStack> shulkerBoxes = new ArrayList<>();

    @EventHandler
    public void  OpenShulker(InventoryClickEvent event){
        //Checks for Permission
        if(event.getWhoClicked().hasPermission("shulkerbox.easyopen")) {
            //Checks if Shift+Right Click and the item is a Shulker Box
            ItemStack item = event.getCurrentItem();
            //Check if a BlockStateMeta First
            if (event.getClick().equals(ClickType.SHIFT_RIGHT) && item != null && item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                //Check if BlockState is that of a ShulkerBox
                if (meta.getBlockState() instanceof ShulkerBox) {
                        //All Good, cancel normal interaction
                        event.setCancelled(true);
                        //Add current gui to prevInvs and the ShulkerBox to shulkerBoxes
                        prevInv.add(event.getView());
                        shulkerBoxes.add(event.getCurrentItem());
                        //Get Box
                        ShulkerBox box = (ShulkerBox) meta.getBlockState();
                        //Create Inventory
                        Inventory inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, "Easy Open Shulker Boxes");
                        //Set Contents
                        inv.setContents(box.getInventory().getContents());
                        //Add to Lists
                        openInvs.add(inv);
                        openSlots.add(event.getRawSlot());
                        //Open Inventory
                        event.getWhoClicked().openInventory(inv);
                }
            }
        }
    }

    @EventHandler
    public void CloseShulker(InventoryCloseEvent event){
        if(openInvs.contains(event.getInventory())){
            int index = openInvs.indexOf(event.getInventory());
            ItemStack[] contents = event.getView().getTopInventory().getStorageContents();
            //ItemStack item = event.getView().getBottomInventory().getItem(openSlots.get(index));
            ItemStack item = shulkerBoxes.get(index);
            if(item.getItemMeta() instanceof BlockStateMeta) {
                //GetBlockState
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                //Check if BlockState is that of a ShulkerBox
                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox box = (ShulkerBox) meta.getBlockState();
                    box.getInventory().setStorageContents(contents);
                    meta.setBlockState(box);
                    item.setItemMeta(meta);
                    prevInv.get(index).setItem(openSlots.get(index), item);
                    //Remove from Lists
                    openInvs.remove(index);
                    openSlots.remove(index);
                    shulkerBoxes.remove(index);
                    prevInv.remove(index);
                }
            }
        }
    }

    @EventHandler
    public void emergencySave(PluginDisableEvent event){
        //For all open inventories, close them starting in the back to the front to avoid index complications
        for(int i = openInvs.size() -1 ; i >= 0 ; i --){
            for(HumanEntity player : openInvs.get(i).getViewers()) {
                this.CloseShulker(new InventoryCloseEvent(player.getOpenInventory()));
            }
        }
    }
}
