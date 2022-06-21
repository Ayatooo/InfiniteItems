package fr.ayato.infiniteitems.utils;

import fr.ayato.infiniteitems.Main;

import java.util.ArrayList;
import java.util.List;

public class Config {

    //get the all the items from the config file
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        for (String s : Main.getInstance().getConfig().getKeys(true)) {
            try {
                if (!Main.getInstance().getConfig().getString(s + ".material").isEmpty()) {
                    items.add(s);
                }
            } catch (NullPointerException ignored) {
            }
        }
        return items;
    }

    //get the item's name
    public static String getItemName(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".name");
    }

    //get the item's material
    public static String getItemMaterial(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".material");
    }

    //get the item's lore
    public static List<String> getItemLore(String item) {
        return Main.getInstance().getConfig().getStringList("items." + item + ".lore");
    }

    //get if the item want the enchants hidden
    public static boolean isEnchantsHidden(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".hide");
    }

    //get the items enchants
    public static List<String> getItemEnchants(String item) {
        List<String> enchants = new ArrayList<>();
        //check if null
        if (Main.getInstance().getConfig().getString("items." + item + ".enchantments") != null) {
            enchants.addAll(Main.getInstance().getConfig().getStringList("items." + item + ".enchantments"));
        }
        return enchants;
    }

    //get the item's enchants level
    public static List<Integer> getItemEnchantsLevel(String item) {
        List<Integer> enchantsLevel = new ArrayList<>();
        //check if null
        if (Main.getInstance().getConfig().getString("items." + item + ".levels") != null) {
            for (String s : Main.getInstance().getConfig().getStringList("items." + item + ".levels")) {
                enchantsLevel.add(Integer.parseInt(s));
            }
        }
        return enchantsLevel;
    }
}
