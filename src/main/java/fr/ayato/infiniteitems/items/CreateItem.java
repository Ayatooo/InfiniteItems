package fr.ayato.infiniteitems.items;

import fr.ayato.infiniteitems.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CreateItem {

    /* Initialize an item with data of the configuration file
       Executed by the command */
    public static ItemStack itemToGive(Material material, String displayName, List<String> loreFromConfig, List<String> enchantments,  List<Integer> levels, Boolean hide, Integer amount) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta itemMeta = item.getItemMeta();

        // Text
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(loreFromConfig);

        // Add Enchantments on the item
        for (int i = 0; i < enchantments.size(); i++) {
            itemMeta.addEnchant(Enchantment.getByName(enchantments.get(i)), levels.get(i), hide);
        }

        // If the option is enable, enchantments will be hide
        if (hide) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemMeta.spigot().setUnbreakable(true);
        item.setItemMeta(itemMeta);
        return item;
    }
}
