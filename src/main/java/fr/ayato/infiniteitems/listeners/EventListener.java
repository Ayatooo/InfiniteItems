package fr.ayato.infiniteitems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.infiniteitems.Main;
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

    public HashMap<ItemMeta, Integer> itemData = new HashMap<>();
    public HashMap<UUID, HashMap<ItemMeta, Integer>> playerList = new HashMap<>();

    // When someone has been killed, checks if this player had an infinite item on him
    @EventHandler
    public void checkOnDeath(PlayerDeathEvent e) {
        final UUID playerUUID = e.getEntity().getPlayer().getUniqueId();
        final Iterator<ItemStack> iterator = e.getDrops().iterator();
        final Player killer = e.getEntity().getKiller();

        final List<String> swordLore = Config.getItemLore("sword");
        //check if the player isnt dead alone

        if (killer != e.getEntity() && killer != null) {
            ItemStack itemStack = killer.getItemInHand();
            NBTItem nbtItem = new NBTItem(killer.getItemInHand());
            nbtItem.getItem();
            Integer kills = nbtItem.getInteger("kills");
            kills++;
            nbtItem.setInteger("kills", kills);
            nbtItem.applyNBT(itemStack);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = Utils.replaceLore(swordLore, "%kills%", String.valueOf(kills));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            killer.updateInventory();
        }

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
                if (configItemLore.get(1).equals(droppedItemLore.get(1))) {
                    if (playerList.containsKey(playerUUID)) {
                        ItemMeta itemMeta = actualItemIterated.getItemMeta();
                        final HashMap<ItemMeta, Integer> playerData = playerList.get(playerUUID);
                        final Integer itemAmount = playerData.get(itemMeta);
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
                        ItemStack itemStack = new ItemStack(material, itemAmount);
                        itemStack.setItemMeta(itemMeta);
                        player.getInventory().addItem(itemStack);
                    }
                }
                itemData.remove(entry.getKey());
                playerList.remove(playerUUID);
            }
        }
    }
}