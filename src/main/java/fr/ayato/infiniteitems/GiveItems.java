package fr.ayato.infiniteitems;

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
            Player player = Bukkit.getPlayer(args[0]);
            String name = Objects.toString(args[1]);
            if (player.hasPermission("infiniteitems") || sender instanceof ConsoleCommandSender) {
                if (plugin.getConfig().contains("items." + name)) {
                    Material material = Material.valueOf(plugin.getConfig().getString("items." + name + ".material"));
                    String lore = plugin.getConfig().getString("items." + name + ".lore");
                    String configItemName = plugin.getConfig().getString("items." + name + ".name");
                    String enchantments = plugin.getConfig().getString("items." + name + ".enchantments");
                    String enchantmentsLevels = plugin.getConfig().getString("items." + name + ".levels");
                    String hide = plugin.getConfig().getString("items." + name + ".hide");
                    player.getInventory().addItem(CreateItem.itemToGive(material, name, Colors.checkForChanges(configItemName), Colors.checkForChanges(lore), enchantments, enchantmentsLevels, hide));
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
