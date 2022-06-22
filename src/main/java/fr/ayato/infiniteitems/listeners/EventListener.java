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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EventListener implements Listener {

    // Plugin
    public Main plugin;
    public EventListener(Main main) {
        this.plugin = main;
    }

    public HashMap<ItemMeta, Integer> itemData = new HashMap<>();
    public HashMap<UUID, HashMap<ItemMeta, Integer>> playerList = new HashMap<>();

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
                        ItemMeta itemMeta = actualItemIterated.getItemMeta();
                        final HashMap<ItemMeta, Integer> playerData = playerList.get(playerUUID);
                        System.out.println("PlayerData : " + playerData);
                        final Integer itemAmount = playerData.get(itemMeta);
                        System.out.println("ItemAmount : " + itemAmount);
                        if (itemAmount != null) {
                            final Integer newAmount = itemAmount + actualItemIterated.getAmount();
                            playerData.put(itemMeta, newAmount);
                            playerList.put(playerUUID, playerData);
                        } else {
                            playerData.put(itemMeta, actualItemIterated.getAmount());
                            playerList.put(playerUUID, playerData);
                        }
                    } else {
                        ItemMeta itemMeta = actualItemIterated.getItemMeta();
                        final HashMap<ItemMeta, Integer> playerData = new HashMap<>();
                        playerData.put(itemMeta, actualItemIterated.getAmount());
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
            for (Map.Entry<ItemMeta, Integer> entry : playerList.get(playerUUID).entrySet()) {
                final ItemMeta itemMeta = entry.getKey();
                final Integer itemAmount = entry.getValue();
                //Get the displayname from the itemMeta
                final String lore = itemMeta.getLore().get(1);
                //check in the config if one item has the same lore
                for (int i = 0; i < Main.configItemName.size(); i++) {
                    final String configItem = Main.configItemName.get(i);
                    String confItem = configItem.replace("items.", "");
                    final List<String> configItemLore = Config.getItemLore(confItem);
                    if (Objects.equals(lore, configItemLore.get(1))) {
                        Material material = Material.valueOf(Config.getItemMaterial(confItem));
                        System.out.println("material : " + material);
                        System.out.println("itemAmount : " + itemAmount);
                        ItemStack itemStack = new ItemStack(material, itemAmount);
                        itemStack.setItemMeta(itemMeta);
                        System.out.println("itemStack : " + itemStack);
                        player.getInventory().addItem(itemStack);
                    }
                }
                itemData.remove(entry.getKey());
                playerList.remove(playerUUID);
            }
        }
    }
}