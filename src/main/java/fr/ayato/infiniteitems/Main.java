package fr.ayato.infiniteitems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static List<String> name = new ArrayList<>();

    // This function active the plugin
    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "*---*_ " + ChatColor.AQUA + "InfiniteItems Enabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
        saveDefaultConfig();
        getCommand("infiniteitems").setExecutor(new GiveItems(this));
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        for (String s : getConfig().getKeys(true)) {
            try {
                if (!getConfig().getString(s + ".material").isEmpty()) {
                    name.add(s);
                }
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
    }

    // This function disable the plugin
    @Override
    public void onDisable(){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE +"*---*_ " + ChatColor.AQUA + "InfiniteItems Disabled !" + ChatColor.LIGHT_PURPLE + " _*---*");
    }
}