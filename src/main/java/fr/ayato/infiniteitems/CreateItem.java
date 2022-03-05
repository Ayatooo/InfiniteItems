package fr.ayato.infiniteitems;

import fr.ayato.infiniteitems.utils.Spliter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CreateItem {

    // Plugin
    private static Main plugin;
    public CreateItem(Main main) {
        this.plugin = main;
    }

    // Initialize an item with data of the configuration file
    // Executed by the command
    public static ItemStack itemToGive(Material material, String name, String displayName, String loreFromConfig, String enchantments,  String levels, String hide) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        // Text
        meta.setDisplayName(displayName);
        meta.setLore(Spliter.strToList(loreFromConfig));

        // Enchantments
        enchantments = enchantments.replace("[", "");
        enchantments = enchantments.replace("]", "");
        List<String> enchantList = Spliter.strToList(enchantments);

        // Enchantments Levels
        levels = levels.replace("[", "");
        levels = levels.replace("]", "");
        List<String> levelsList = Spliter.strToList(levels);

        // Add enchants on the item
        for (int i = 0; i < enchantList.size(); i++) {
            meta.addEnchant(Enchantment.getByName(enchantList.get(i)), Integer.parseInt(levelsList.get(i)), true);
        }

        if (hide.contains("true")) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
}
