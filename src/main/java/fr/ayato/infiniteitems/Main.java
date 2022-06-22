package fr.ayato.infiniteitems;

import fr.ayato.infiniteitems.items.GiveItems;
import fr.ayato.infiniteitems.listeners.EventListener;
import fr.ayato.infiniteitems.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static List<String> configItemName = new ArrayList<>();

    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }

    // This function active the plugin
    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "*---*_ " + ChatColor.AQUA + "InfiniteItems Enabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
        saveDefaultConfig();
        getCommand("infiniteitems").setExecutor(new GiveItems(this));
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        configItemName = Config.getAllItems();
    }

    // This function disable the plugin
    @Override
    public void onDisable(){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE +"*---*_ " + ChatColor.AQUA + "InfiniteItems Disabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
    }
}