package fr.ayato.infiniteitems.colors;

import org.bukkit.ChatColor;

public class ChangeColors {

    public static String checkForChanges(String lore) {
        lore = lore.replaceAll("&0", ChatColor.BLACK + "");
        lore = lore.replaceAll("&1", ChatColor.DARK_BLUE + "");
        lore = lore.replaceAll("&2", ChatColor.DARK_GREEN + "");
        lore = lore.replaceAll("&3", ChatColor.DARK_AQUA + "");
        lore = lore.replaceAll("&4", ChatColor.DARK_RED + "");
        lore = lore.replaceAll("&5", ChatColor.DARK_PURPLE + "");
        lore = lore.replaceAll("&6", ChatColor.GOLD + "");
        lore = lore.replaceAll("&7", ChatColor.GRAY + "");
        lore = lore.replaceAll("&8", ChatColor.DARK_GRAY+ "");
        lore = lore.replaceAll("&9", ChatColor.BLUE + "");
        lore = lore.replaceAll("&a", ChatColor.GREEN + "");
        lore = lore.replaceAll("&b", ChatColor.AQUA + "");
        lore = lore.replaceAll("&c", ChatColor.RED + "");
        lore = lore.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
        lore = lore.replaceAll("&e", ChatColor.YELLOW + "");
        lore = lore.replaceAll("&f", ChatColor.WHITE + "");
        lore = lore.replaceAll("&g", ChatColor.MAGIC + "");
        return lore;
    }

}
