package cc.akashic.insight.utils;

import cc.akashic.insight.Insight;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;

public final class ItemsViewer {
    private static final HashSet<Inventory> protectedInventoryList = new HashSet<>();

    /**
     * Print the items to console.
     *
     * @param items items to print
     */
    public static void printToConsole(ItemStack[] items) {
        boolean isEmpty = true;

        for (ItemStack item : items) {
            if (item != null) {
                Material material = item.getType();
                if (material == Material.AIR) {
                    continue;
                }

                isEmpty = false;
                Map<Enchantment, Integer> enchantments = item.getEnchantments();

                StringBuilder message = new StringBuilder(material + " x" + item.getAmount() + " ");

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    message.append("| ").append(entry.getKey().getKey()).append(" ").append(entry.getValue()).append(" ");
                }

                Bukkit.getLogger().info(message.toString());
            }
        }

        if (isEmpty) {
            Bukkit.getLogger().info("Nothing!");
        }
    }

    /**
     * Open a GUI for player to view the items.
     * The click event of the GUI is disabled.
     * Forced to close after ticks.
     *
     * @param items        items to view
     * @param playerToSend player to send
     * @param size         a multiple of 9 as the size of inventory to create
     * @param title        the title of the inventory
     * @param ticks        when to close the inventory
     */
    public static void guiToPlayer(ItemStack[] items, Player playerToSend, int size, String title, long ticks) {
        Inventory inventory = Bukkit.createInventory(null, size);

        for (ItemStack item : items) {
            if (item != null) {
                Material material = item.getType();
                if (material == Material.AIR) {
                    continue;
                }

                inventory.addItem(item);
            }
        }

        protectedInventoryList.add(inventory);
        playerToSend.openInventory(inventory);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> {
            InventoryView playerOpenInventory = playerToSend.getOpenInventory();
            if (playerOpenInventory.getTopInventory() == inventory || playerOpenInventory.getTopInventory() == inventory) {
                playerToSend.closeInventory();
            }

            protectedInventoryList.remove(inventory);
        }, ticks);
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onInventoryClick(InventoryClickEvent event) {
            if (protectedInventoryList.contains(event.getInventory())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
