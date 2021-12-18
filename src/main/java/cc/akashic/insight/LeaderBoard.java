package cc.akashic.insight;

import cc.akashic.insight.utils.ItemsViewer;
import cc.akashic.insight.utils.PlayerHead;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public final class LeaderBoard {
    private static final ArrayList<Inventory> protectedInventoryList = new ArrayList<>();

    public static void openToPlayer(Player player) {
        Material itemType = player.getInventory().getItemInMainHand().getType();

        if (itemType == Material.AIR) {
            player.sendMessage("You must hold something in your hand!");
            return;
        }

        selectStatisticToPlayer(player, itemType);
    }

    private static void selectStatisticToPlayer(Player player, Material material) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Select Statistic Type");

        inventory.addItem(new ItemStack(material));
        inventory.addItem(new ItemStack(Material.BARRIER));

        ItemStack itemMined = new ItemStack(Material.BLACK_SHULKER_BOX);
        ItemMeta itemMinedMeta = itemMined.getItemMeta();
        assert itemMinedMeta != null;
        itemMinedMeta.setDisplayName("Mined");
        itemMined.setItemMeta(itemMinedMeta);
        inventory.addItem(itemMined);

        ItemStack itemBroken = new ItemStack(Material.BLUE_SHULKER_BOX);
        ItemMeta itemBrokenMeta = itemMined.getItemMeta();
        assert itemBrokenMeta != null;
        itemBrokenMeta.setDisplayName("Broken");
        itemBroken.setItemMeta(itemBrokenMeta);
        inventory.addItem(itemBroken);

        ItemStack itemCrafted = new ItemStack(Material.BROWN_SHULKER_BOX);
        ItemMeta itemCraftedMeta = itemMined.getItemMeta();
        assert itemCraftedMeta != null;
        itemCraftedMeta.setDisplayName("Crafted");
        itemCrafted.setItemMeta(itemCraftedMeta);
        inventory.addItem(itemCrafted);

        ItemStack itemUsed = new ItemStack(Material.CYAN_SHULKER_BOX);
        ItemMeta itemUsedMeta = itemMined.getItemMeta();
        assert itemUsedMeta != null;
        itemUsedMeta.setDisplayName("Used");
        itemUsed.setItemMeta(itemUsedMeta);
        inventory.addItem(itemUsed);

        ItemStack itemPickUp = new ItemStack(Material.GRAY_SHULKER_BOX);
        ItemMeta itemPickUpMeta = itemMined.getItemMeta();
        assert itemPickUpMeta != null;
        itemPickUpMeta.setDisplayName("Pick Up");
        itemPickUp.setItemMeta(itemPickUpMeta);
        inventory.addItem(itemPickUp);

        ItemStack itemDropped = new ItemStack(Material.GREEN_SHULKER_BOX);
        ItemMeta itemDroppedMeta = itemMined.getItemMeta();
        assert itemDroppedMeta != null;
        itemDroppedMeta.setDisplayName("Dropped");
        itemDropped.setItemMeta(itemDroppedMeta);
        inventory.addItem(itemDropped);

        protectedInventoryList.add(inventory);
        player.openInventory(inventory);
    }

    private static OfflinePlayer[] sortOfflinePlayers(Statistic statistic, Material material, boolean isReverse) {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

        if (isReverse) {
            Arrays.sort(offlinePlayers, Comparator.comparingInt((OfflinePlayer offlinePlayer) -> offlinePlayer.getStatistic(statistic, material)).reversed());
        } else {
            Arrays.sort(offlinePlayers, Comparator.comparingInt((OfflinePlayer offlinePlayer) -> offlinePlayer.getStatistic(statistic, material)));
        }

        return offlinePlayers;
    }

    private static void displayLeaderBoardToPlayer(Player player, Statistic statistic, Material material, boolean isReverse) {
        OfflinePlayer[] offlinePlayersSorted = sortOfflinePlayers(statistic, material, true);
        ArrayList<ItemStack> items = new ArrayList<>();

        int i = 1;
        for (OfflinePlayer offlinePlayer : offlinePlayersSorted) {
            items.add(PlayerHead.getPlayerHead(offlinePlayer, offlinePlayer.getName()));

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;

            meta.setDisplayName(ChatColor.DARK_AQUA + offlinePlayer.getName());
            ArrayList<String> lore = new ArrayList<>();
            lore.add("No. " + i);
            lore.add("Value: " + offlinePlayer.getStatistic(statistic, material));
            meta.setLore(lore);
            item.setItemMeta(meta);

            items.add(item);

            if (i % 4 == 0) {
                ItemStack barrier = new ItemStack(Material.BARRIER);
                ItemMeta barrierMeta = barrier.getItemMeta();
                assert barrierMeta != null;
                barrierMeta.setDisplayName(String.valueOf(i));
                barrier.setItemMeta(barrierMeta);
                items.add(barrier);
            }

            i++;
            if (i > 27) {
                break;
            }
        }

        ItemsViewer.guiToPlayer(items.toArray(new ItemStack[0]), player, 54, "Leader Board " + statistic.name(), 1200L);
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onInventoryClick(InventoryClickEvent event) {
            Inventory inventory = event.getInventory();

            if (protectedInventoryList.contains(inventory)) {
                event.setResult(Event.Result.DENY);

                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null) {
                    return;
                }

                Material material = currentItem.getType();
                Statistic statistic;

                switch (material) {
                    case BLACK_SHULKER_BOX -> statistic = Statistic.MINE_BLOCK;

                    case BLUE_SHULKER_BOX -> statistic = Statistic.BREAK_ITEM;

                    case BROWN_SHULKER_BOX -> statistic = Statistic.CRAFT_ITEM;

                    case CYAN_SHULKER_BOX -> statistic = Statistic.USE_ITEM;

                    case GRAY_SHULKER_BOX -> statistic = Statistic.PICKUP;

                    case GREEN_SHULKER_BOX -> statistic = Statistic.DROP;

                    default -> {
                        return;
                    }
                }

                protectedInventoryList.remove(inventory);
                displayLeaderBoardToPlayer((Player) event.getWhoClicked(), statistic, Objects.requireNonNull(inventory.getItem(0)).getType(), true);
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onInventoryClose(InventoryCloseEvent event) {
            protectedInventoryList.remove(event.getInventory());
        }
    }
}
