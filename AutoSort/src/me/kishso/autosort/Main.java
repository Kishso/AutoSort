package me.kishso.autosort;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(new AutoSortEvents(), this);
        //Register Command(s)
        this.getCommand("autosort").setExecutor(new GiveBookCommand());

    }

    @Override
    public void onDisable(){

    }
}
