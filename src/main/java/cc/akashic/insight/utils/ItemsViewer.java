package cc.akashic.insight.utils;

import cc.akashic.insight.Insight;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
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
        boolean doTranslate = false;
        JsonObject transJsonObject = null;

        // Check if minecraft_lang.json exists
        try {
            FileReader reader = new FileReader(Insight.dataFolder + "/minecraft_lang.json");
            doTranslate = true;
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);
            transJsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException ignored) {
        }

        for (ItemStack item : items) {
            if (item != null) {
                Material material = item.getType();
                if (material == Material.AIR) {
                    continue;
                }

                isEmpty = false;

                // Translate the material name
                String materialName = material.toString();
                if (doTranslate) {
                    try {
                        materialName = transJsonObject.get(("item.minecraft." + material.toString().toLowerCase())).getAsString();
                    } catch (NullPointerException ignoredItem) {
                        try {
                            materialName = transJsonObject.get(("block.minecraft." + material.toString().toLowerCase())).getAsString();
                        } catch (NullPointerException ignoredBlock) {
                        }
                    }
                }

                Map<Enchantment, Integer> enchantments = item.getEnchantments();

                StringBuilder message = new StringBuilder(materialName + " x" + item.getAmount() + " ");

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    // Translate the enchantment name
                    String enchantmentName = entry.getKey().getKey().toString();
                    if (doTranslate) {
                        try {
                            enchantmentName = transJsonObject.get(("enchantment." + entry.getKey().getKey().toString().replace(":", "."))).getAsString();
                        } catch (NullPointerException ignored) {
                        }
                    }
                    message.append("| ").append(enchantmentName).append(" ").append(entry.getValue()).append(" ");
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
