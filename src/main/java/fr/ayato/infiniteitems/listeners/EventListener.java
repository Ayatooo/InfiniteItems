package fr.ayato.infiniteitems.listeners;

import fr.ayato.infiniteitems.Main;
import fr.ayato.infiniteitems.items.CreateItem;
import fr.ayato.infiniteitems.utils.Config;
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

    public HashMap<String, Integer> itemData = new HashMap<>();
    public HashMap<UUID, HashMap<String, Integer>> playerList = new HashMap<>();

    // When someone has been killed, checks if this player had an infinite item on him
    @EventHandler
    public void checkOnDeath(PlayerDeathEvent e) {
        final UUID playerUUID = e.getEntity().getPlayer().getUniqueId();
        final Iterator<ItemStack> iterator = e.getDrops().iterator();

        // Iterate over all the player inventory to check if he has infinite item
        while(iterator.hasNext()){
            final ItemStack actualItemIterated = iterator.next();

            // If the item is an infinite item, remove it from the inventory and add it to the playerList
            for (int i = 0; i < Main.configItemName.size(); i++) {
                final String configItem = Main.configItemName.get(i);
                String confItem = configItem.replace("items.", "");
                final List<String> configItemLore = Config.getItemLore(confItem);
                final List<String> droppedItemLore = actualItemIterated.getItemMeta().getLore();

                // Searching for infinite items
                if (configItemLore.equals(droppedItemLore)) {
                    if (playerList.containsKey(playerUUID)) {
                        final HashMap<String, Integer> playerData = playerList.get(playerUUID);
                        final Integer itemAmount = playerData.get(confItem);
                        if (itemAmount != null) {
                            final Integer newAmount = itemAmount + actualItemIterated.getAmount();
                            playerData.put(confItem, newAmount);
                            playerList.put(playerUUID, playerData);
                        } else {
                            playerData.put(confItem, actualItemIterated.getAmount());
                            playerList.put(playerUUID, playerData);
                        }
                    } else {
                        final HashMap<String, Integer> playerData = new HashMap<>();
                        playerData.put(confItem, actualItemIterated.getAmount());
                        playerList.put(playerUUID, playerData);
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

        // If the player is in the list, give him his infinite's items
        if (playerList.containsKey(playerUUID)) {
            for (Map.Entry<String, Integer> entry : playerList.get(playerUUID).entrySet()) {
                final String itemName = entry.getKey().replace("items.", "");
                final Integer amount = entry.getValue();
                final Material material = Material.getMaterial(Config.getItemMaterial(itemName));
                final String displayName = Config.getItemName(itemName);
                final List<String> lore = Config.getItemLore(itemName);
                final List<String> enchantments = Config.getItemEnchants(itemName);
                final List<Integer> levels = Config.getItemEnchantsLevel(itemName);
                final Boolean hide = Config.isEnchantsHidden(itemName);
                final ItemStack item = CreateItem.itemToGive(material, displayName, lore, enchantments, levels, hide, amount);
                player.getInventory().addItem(item);
                itemData.remove(itemName);
                playerList.remove(playerUUID);
            }
        }
    }
}