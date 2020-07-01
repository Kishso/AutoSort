package me.kishso.easyopenshulkerboxes;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(new CustomShulkerEvents(),this);
    }

    @Override
    public void onDisable(){
    }
}
