package fr.ayato.infiniteitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE +"*---*_ " + ChatColor.AQUA + "InfiniteItems Enabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
        saveDefaultConfig();

        getCommand("infiniteitems").setExecutor(new GiveItems(this));
        //getServer().getPluginManager().registerEvents(new OnDeath(), this);
        //getServer().getPluginManager().registerEvents(new OnRespawn(), this);
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE +"*---*_ " + ChatColor.AQUA + "InfiniteItems Disabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
    }
}