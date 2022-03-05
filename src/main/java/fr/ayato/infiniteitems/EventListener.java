package fr.ayato.infiniteitems;

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

    public HashMap<UUID, List<String>> hasInfiniteItem = new HashMap<>();
    // When someone has been killed, checks if this player had an infinite item on him
    @EventHandler
    public void checkOnDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Iterate over all the player inventory to check if he has infinite item
        Iterator<ItemStack> iterator = e.getDrops().iterator();
        while(iterator.hasNext()){
            ItemStack stack = iterator.next();
            for (int i = 0; i < Main.name.size(); i++) {
                List<String> loreToCheck = Spliter.strToList(Spliter.stringToSplit(plugin.getConfig().getString(Main.name.get(i) + ".lore")));
                List<String> loreOfItemDropped = stack.getItemMeta().getLore();

                if (loreToCheck.equals(loreOfItemDropped)) {
                    System.out.println("lore egal = " + loreOfItemDropped);
                    if (hasInfiniteItem.containsKey(playerUUID)) {

                        for (Map.Entry<UUID, List<String>> entry : hasInfiniteItem.entrySet()) {
                            List<String> temp = new ArrayList<>();
                            for (String s : entry.getValue()) {
                                temp.add(s);
                                System.out.println("temp DANS BOUCLE = " + temp);
                            }
                            System.out.println("temp HORS BOUCLE = " + temp);
                            hasInfiniteItem.put(playerUUID, temp);
                        }

                    } else {
                        hasInfiniteItem.put(playerUUID, Collections.singletonList(Main.name.get(i)));
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
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Iterate over the list until it's empty
        while (!hasInfiniteItem.isEmpty()) {
            System.out.println("LA MAP = " + hasInfiniteItem);
            for (Map.Entry<UUID, List<String>> entry : hasInfiniteItem.entrySet()) {
                for (String s : entry.getValue()) {
                    // Item's Data
                    Material material = Material.valueOf(plugin.getConfig().getString(s + ".material"));
                    String lore = plugin.getConfig().getString(s + ".lore");
                    String configItemName = plugin.getConfig().getString(s + ".name");
                    String enchantments = plugin.getConfig().getString(s + ".enchantments");
                    String enchantmentsLevels = plugin.getConfig().getString(s + ".levels");
                    String hide = plugin.getConfig().getString(s + ".hide");

                    // Give the item to the player who lost it
                    player.getInventory().addItem(CreateItem.itemToGive(material, Spliter.itemPathToName(s), Colors.checkForChanges(configItemName), Colors.checkForChanges(lore), enchantments, enchantmentsLevels, hide));
                    player.updateInventory();
                }
                hasInfiniteItem.remove(playerUUID);
            }
        }
    }
}

/*for (Map.Entry<UUID, List<String>> entry : hasInfiniteItem.entrySet()) {
                            for (String s : entry.getValue()) {

                            }
                        }*/