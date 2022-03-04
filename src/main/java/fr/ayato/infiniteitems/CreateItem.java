package fr.ayato.infiniteitems;

import fr.ayato.infiniteitems.colors.ChangeColors;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CreateItem {

    // Initialize an item with data of the configuration file
    // Executed by the command
    public static ItemStack itemToGive(Material material, String displayName, String loreFromConfig) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> loreToSet = new ArrayList<>();
        Stream.of(loreFromConfig)
                .map(s -> s.split("&&& "))
                .flatMap(Stream::of)
                .forEach(loreToSet::add);
        meta.setLore(loreToSet);

        // FAIRE LES ENCHANTEMENTS
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
}
