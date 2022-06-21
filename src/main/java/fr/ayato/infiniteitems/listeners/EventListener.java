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

        final List<String> coItLore = Config.getItemLore("sword");
        //check if the player isnt dead alone

        if (killer != e.getEntity() && killer != null) {
            System.out.println("KILLER != PLAYER");
            ItemStack itemStack = killer.getItemInHand();
            NBTItem nbtItem = new NBTItem(killer.getItemInHand());
            nbtItem.getItem();
            System.out.println("kills : " + nbtItem.getInteger("kills"));
            Integer kills = nbtItem.getInteger("kills");
            kills++;
            nbtItem.setInteger("kills", kills);
            nbtItem.applyNBT(itemStack);
            System.out.println("NewKills : " + kills);
            System.out.println("NewKillsON SWORD : " + nbtItem.getInteger("kills"));
            System.out.println("itemMeta : " + itemStack.getItemMeta());
            ItemStack itemStack2 = new ItemStack(Material.DIAMOND_SWORD);
            itemStack2.setItemMeta(itemStack.getItemMeta());
            killer.getInventory().addItem(itemStack2);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();

            for (String s : coItLore) {
                if (s.contains("%kills%")) {

                    s = s.replace("%kills%", kills.toString());
                }
                lore.add(s);
            }

            System.out.println("lore : " + lore);
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
                if (configItemLore.contains(droppedItemLore.get(1))) {
                    System.out.println("Item found");
                    if (playerList.containsKey(playerUUID)) {
                        final HashMap<ItemMeta, Integer> playerData = playerList.get(playerUUID);
                        final Integer itemAmount = playerData.get(confItem);
                        if (itemAmount != null) {
                            final Integer newAmount = itemAmount + actualItemIterated.getAmount();
                            //get the itemMeta of the item
                            final ItemMeta itemMeta = actualItemIterated.getItemMeta();
                            playerData.put(itemMeta, newAmount);
                            playerList.put(playerUUID, playerData);
                        } else {
                            final ItemMeta itemMeta = actualItemIterated.getItemMeta();
                            playerData.put(itemMeta, actualItemIterated.getAmount());
                            playerList.put(playerUUID, playerData);
                        }
                    } else {
                        final ItemMeta itemMeta = actualItemIterated.getItemMeta();
                        final HashMap<ItemMeta, Integer> playerData = new HashMap<>();
                        playerData.put(itemMeta, actualItemIterated.getAmount());
                        playerList.put(playerUUID, playerData);
                    }
                    System.out.println("itemData : " + itemData);
                    System.out.println("playerList : " + playerList);
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