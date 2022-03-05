package fr.ayato.infiniteitems.listeners;

import fr.ayato.infiniteitems.Main;
import fr.ayato.infiniteitems.items.CreateItem;
import fr.ayato.infiniteitems.utils.Colors;
import fr.ayato.infiniteitems.utils.Spliter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class EventListener implements Listener {

    // Plugin
    public Main plugin;
    public EventListener(Main main) {
        this.plugin = main;
    }

    private HashMap<UUID, List<String>> playersInfiniteItems = new HashMap<>();

    // When someone has been killed, checks if this player had an infinite item on him
    @EventHandler
    public void checkOnDeath(PlayerDeathEvent e) {
        final UUID playerUUID = e.getEntity().getPlayer().getUniqueId();

        // Allow to loop over dropped items
        final Iterator<ItemStack> iterator = e.getDrops().iterator();

        // Iterate over all the player inventory to check if he has infinite item
        while(iterator.hasNext()){
            final ItemStack actualItemIterated = iterator.next();

            for (int i = 0; i < Main.configItemName.size(); i++) {
                final String configItem = Main.configItemName.get(i);
                final List<String> configItemLore = Spliter.strToList(Spliter.stringToSplit(plugin.getConfig().getString(configItem + ".lore")));
                final List<String> droppedItemLore = actualItemIterated.getItemMeta().getLore();

                // Searching for infinite items
                if (configItemLore.equals(droppedItemLore)) {
                    if (playersInfiniteItems.containsKey(playerUUID)) {

                        // Put the player in a Hashmap if he has one / multiple infinite items
                        List<String> playerItemsList = new ArrayList<>(playersInfiniteItems.get(playerUUID));
                        playerItemsList.add(configItem);
                        playersInfiniteItems.put(playerUUID, playerItemsList);
                    } else {
                        playersInfiniteItems.put(playerUUID, Collections.singletonList(configItem));
                    }
                    iterator.remove();
                }
            }
        }
    }

    /* When someone has respawned, check if the list of player is null
    Give to a player his infinite item if he is in the list */
    @EventHandler
    public void checkOnRespawn(PlayerRespawnEvent e) {
        final Player player = e.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        // Iterate over the list until it's empty
        while (!playersInfiniteItems.isEmpty()) {
            for (Map.Entry<UUID, List<String>> entry : playersInfiniteItems.entrySet()) {
                for (String s : entry.getValue()) {
                    // Item's Data
                    final Material material = Material.valueOf(plugin.getConfig().getString(s + ".material"));
                    final String lore = plugin.getConfig().getString(s + ".lore");
                    final String itemDisplayName = plugin.getConfig().getString(s + ".name");
                    final String enchantments = plugin.getConfig().getString(s + ".enchantments");
                    final String enchantmentsLevels = plugin.getConfig().getString(s + ".levels");
                    final String hide = plugin.getConfig().getString(s + ".hide");

                    // Give the item to the player who lost it
                    player.getInventory().addItem(CreateItem.itemToGive(material, Colors.checkForChanges(itemDisplayName), Colors.checkForChanges(lore), enchantments, enchantmentsLevels, hide));
                    player.updateInventory();
                }
                playersInfiniteItems.remove(playerUUID);
            }
        }
    }
}
