package fr.ayato.infiniteitems.items;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.infiniteitems.Main;
import fr.ayato.infiniteitems.utils.Config;
import fr.ayato.infiniteitems.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiveItems implements CommandExecutor {

    // Plugin
    private final Main plugin;
    public GiveItems(Main main) {
        this.plugin = main;
    }

    // Executed when a player type the /infiniteitems command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 3) {
            final Player player = Bukkit.getPlayer(args[0]);
            final String configItemName = Objects.toString(args[1]);
            Integer number = Integer.parseInt(args[2]);

            /* Only a player with the permission or a player buying / executing the command with console can
               have an item existing in the config file */
            if (player.hasPermission("infiniteitems") || sender instanceof ConsoleCommandSender) {
                if (plugin.getConfig().contains("items." + configItemName)) {

                    // Item's Data
                    final Material material = Material.valueOf(Config.getItemMaterial(configItemName));
                    final List<String> loreFromConfig = Config.getItemLore(configItemName);
                    final String itemDisplayName = Config.getItemName(configItemName);
                    final List<String> enchantments = Config.getItemEnchants(configItemName);
                    final List<Integer> enchantmentsLevels = Config.getItemEnchantsLevel(configItemName);
                    final Boolean hide = Config.isEnchantsHidden(configItemName);
                    ItemStack itemStack = CreateItem.itemToGive(material, itemDisplayName, loreFromConfig, enchantments, enchantmentsLevels, hide, number);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    NBTItem nbtItem = new NBTItem(itemStack);
                    nbtItem.getItem();
                    List<String> lore = Utils.replaceLore(loreFromConfig, "%kills%", "0");
                    lore = Utils.replaceLore(lore, "%date%", Utils.getDate());
                    itemMeta.setLore(lore);
                    nbtItem.setInteger("kills", 0);
                    nbtItem.applyNBT(itemStack);
                    nbtItem.setString("date", Utils.getDate());
                    nbtItem.applyNBT(itemStack);
                    System.out.println("DATE 1 : " + nbtItem.getString("date"));
                    itemStack.setItemMeta(itemMeta);

                    // Give the item to the player
                    player.getInventory().addItem(itemStack);
                    player.updateInventory();
                } else {
                    player.sendMessage("§b§lInfiniteItems §e» §cPlease select an item existing in the configuration file !");
                }
            }
        } else {
            sender.sendMessage("§b§lInfiniteItems §e» §f/infiniteitems <pseudo> <type> <nombre>");
        }
        return false;
    }
}