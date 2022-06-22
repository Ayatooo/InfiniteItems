package fr.ayato.infiniteitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.infiniteitems.Main;
import fr.ayato.infiniteitems.items.CreateItem;
import fr.ayato.infiniteitems.utils.Config;
import fr.ayato.infiniteitems.utils.Utils;
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
        final Player killer = e.getEntity().getKiller();
        List<String> lore = new ArrayList<>();

        if (killer != e.getEntity()) {
            ItemStack killerItem = killer.getItemInHand();
            NBTItem nbtItem = new NBTItem(killerItem);
            if (nbtItem.hasKey("infinite")) {
                Material material = killerItem.getType();
                switch (material) {
                    case DIAMOND_SWORD:
                        ItemStack itemStack = killer.getItemInHand();
                        nbtItem.getItem();
                        nbtItem.setInteger("kills", nbtItem.getInteger("kills") + 1);
                        nbtItem.applyNBT(itemStack);
                        List<String> loreFromConfig = Config.getItemLore("sword");
                        for (String s : loreFromConfig) {
                            if (s.contains("%kills%")) {
                                s = s.replace("%kills%", String.valueOf(nbtItem.getInteger("kills")));
                            }
                            lore.add(s);
                        }
                        ItemMeta itemMeta = killerItem.getItemMeta();
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                        killer.updateInventory();
                        break;
                }
            }
        }

        // Iterate over all the player inventory to check if he has infinite item
        while(iterator.hasNext()){
            final ItemStack actualItemIterated = iterator.next();

            // If the item is an infinite item, remove it from the inventory and add it to the playerList
            for (int i = 0; i < Main.configItemName.size(); i++) {
                final String configItem = Main.configItemName.get(i);
                final List<String> configItemLore = Config.getItemLore(configItem.replace("items.", ""));
                final List<String> droppedItemLore = actualItemIterated.getItemMeta().getLore();

                // Searching for infinite items
                if (configItemLore.contains(droppedItemLore.get(1))) {
                    if (playerList.containsKey(playerUUID)) {
                        final HashMap<ItemMeta, Integer> playerData = playerList.get(playerUUID);
                        final Integer itemAmount = playerData.get(configItem.replace("items.", ""));
                        if (itemAmount != null) {
                            final Integer newAmount = itemAmount + actualItemIterated.getAmount();
                            //get the itemMeta of the item
                            final ItemMeta actualitemMeta = actualItemIterated.getItemMeta();
                            playerData.put(actualitemMeta, newAmount);
                            playerList.put(playerUUID, playerData);
                        } else {
                            final ItemMeta actualitemMeta = actualItemIterated.getItemMeta();
                            playerData.put(actualitemMeta, actualItemIterated.getAmount());
                            playerList.put(playerUUID, playerData);
                        }
                    } else {
                        final ItemMeta actualitemMeta = actualItemIterated.getItemMeta();
                        final HashMap<ItemMeta, Integer> playerData = new HashMap<>();
                        playerData.put(actualitemMeta, actualItemIterated.getAmount());
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
                final ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
                itemData.remove(itemMeta);
                playerList.remove(playerUUID);
            }
        }
    }

}