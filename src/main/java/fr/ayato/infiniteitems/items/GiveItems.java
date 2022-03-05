package fr.ayato.infiniteitems.items;

import fr.ayato.infiniteitems.Main;
import fr.ayato.infiniteitems.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
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
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[0]);
            final String configItemName = Objects.toString(args[1]);

            /* Only a player with the permission or a player buying / executing the command with console can
               have an item existing in the config file */
            if (player.hasPermission("infiniteitems") || sender instanceof ConsoleCommandSender) {
                if (plugin.getConfig().contains("items." + configItemName)) {

                    // Item's Data
                    final Material material = Material.valueOf(plugin.getConfig().getString("items." + configItemName + ".material"));
                    final String lore = plugin.getConfig().getString("items." + configItemName + ".lore");
                    final String itemDisplayName = plugin.getConfig().getString("items." + configItemName + ".name");
                    final String enchantments = plugin.getConfig().getString("items." + configItemName + ".enchantments");
                    final String enchantmentsLevels = plugin.getConfig().getString("items." + configItemName + ".levels");
                    final String hide = plugin.getConfig().getString("items." + configItemName + ".hide");

                    // Give the item to the player
                    player.getInventory().addItem(CreateItem.itemToGive(material, Colors.checkForChanges(itemDisplayName), Colors.checkForChanges(lore), enchantments, enchantmentsLevels, hide));
                    player.updateInventory();
                } else {
                    player.sendMessage("§b§lInfiniteItems §e» §cPlease select an item existing in the configuration file !");
                }
            }
        } else {
            sender.sendMessage("§b§lInfiniteItems §e» §b/infiniteitems <pseudo> <type>");
        }
        return false;
    }
}
