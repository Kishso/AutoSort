package me.kishso.autosort;

import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class GiveBookCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //Creates A Book ItemStack that fulfills required conditions.
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setPages(new String[54]);
        meta.setAuthor("AutoSort Plugin");
        //Option to set the Title of the Book
        if(strings.length > 0 && strings[0] != null){
            meta.setTitle(strings[0]);
        }else {
            meta.setTitle("AutoSort Plugin Testing");
        }
        book.setItemMeta(meta);
        //Gives the player the ItemStack if sender is a player
        if(commandSender instanceof Player){
            Player player = (Player)commandSender;
            player.getInventory().addItem(book);
            return true;
        }

        return false;
    }
}
