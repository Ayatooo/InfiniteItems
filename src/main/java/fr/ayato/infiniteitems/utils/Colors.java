package fr.ayato.infiniteitems.utils;

import org.bukkit.ChatColor;

public class Colors {

    // Change the characters from the config file to minecraft Colors
    public static String checkForChanges(String s) {
        s = s.replaceAll("&0", ChatColor.BLACK + "");
        s = s.replaceAll("&1", ChatColor.DARK_BLUE + "");
        s = s.replaceAll("&2", ChatColor.DARK_GREEN + "");
        s = s.replaceAll("&3", ChatColor.DARK_AQUA + "");
        s = s.replaceAll("&4", ChatColor.DARK_RED + "");
        s = s.replaceAll("&5", ChatColor.DARK_PURPLE + "");
        s = s.replaceAll("&6", ChatColor.GOLD + "");
        s = s.replaceAll("&7", ChatColor.GRAY + "");
        s = s.replaceAll("&8", ChatColor.DARK_GRAY+ "");
        s = s.replaceAll("&9", ChatColor.BLUE + "");
        s = s.replaceAll("&a", ChatColor.GREEN + "");
        s = s.replaceAll("&b", ChatColor.AQUA + "");
        s = s.replaceAll("&c", ChatColor.RED + "");
        s = s.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
        s = s.replaceAll("&e", ChatColor.YELLOW + "");
        s = s.replaceAll("&f", ChatColor.WHITE + "");
        s = s.replaceAll("&g", ChatColor.MAGIC + "");
        return s;
    }

}
