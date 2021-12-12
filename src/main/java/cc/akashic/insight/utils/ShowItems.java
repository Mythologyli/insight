package cc.akashic.insight.utils;

import cc.akashic.insight.Insight;
import me.pikamug.localelib.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class ShowItems {
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

                StringBuilder message = new StringBuilder(ChatColor.YELLOW + material.toString() + ChatColor.RESET + " x" + item.getAmount() + " " + ChatColor.LIGHT_PURPLE);

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    message.append("| ").append(entry.getKey().getKey()).append(" ").append(entry.getValue()).append(" ");
                }

                Bukkit.getLogger().info(message.toString());
            }
        }

        if (isEmpty) {
            Bukkit.getLogger().info(ChatColor.YELLOW + "Nothing!");
        }
    }

    public static void printToPlayer(ItemStack[] items, Player playerToSend) {
        LocaleManager localeManager = Insight.getLocaleManager();

        boolean isEmpty = true;

        for (ItemStack item : items) {
            if (item != null) {
                Material material = item.getType();
                if (material == Material.AIR) {
                    continue;
                }

                isEmpty = false;
                Map<Enchantment, Integer> enchantments = item.getEnchantments();

                StringBuilder message = new StringBuilder(ChatColor.YELLOW + "<item>" + ChatColor.RESET + "    x" + item.getAmount() + "    " + ChatColor.LIGHT_PURPLE);

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    message.append("| <enchantment>").append("  ");
                }

                localeManager.sendMessage(playerToSend, message.toString(), material, (short) 0, enchantments);
            }
        }

        if (isEmpty) {
            playerToSend.sendMessage(ChatColor.YELLOW + "Nothing!");
        }
    }
}
