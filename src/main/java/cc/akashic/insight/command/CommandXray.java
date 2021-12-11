package cc.akashic.insight.command;

import cc.akashic.insight.Insight;
import me.pikamug.localelib.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CommandXray implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage("Player not found!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");
        sender.sendMessage("Player: " + playerName);

        ItemStack[] playerItems = player.getInventory().getContents();
        boolean isInventoryEmpty = true;

        if (sender instanceof Player) {
            LocaleManager localeManager = Insight.getLocaleManager();

            for (ItemStack item : playerItems) {
                if (item != null) {
                    isInventoryEmpty = false;
                    Material material = item.getType();
                    Map<Enchantment, Integer> enchantments = item.getEnchantments();

                    StringBuilder message = new StringBuilder(ChatColor.YELLOW + "<item>" + ChatColor.RESET + "    x" + item.getAmount() + "    " + ChatColor.LIGHT_PURPLE);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        message.append("| <enchantment>").append("  ");
                    }

                    localeManager.sendMessage((Player) sender, message.toString(), material, (short) 0, enchantments);
                }
            }
        } else {
            for (ItemStack item : playerItems) {
                if (item != null) {
                    isInventoryEmpty = false;
                    Material material = item.getType();
                    Map<Enchantment, Integer> enchantments = item.getEnchantments();

                    StringBuilder message = new StringBuilder(ChatColor.YELLOW + material.toString() + ChatColor.RESET + " x" + item.getAmount() + " " + ChatColor.LIGHT_PURPLE);

                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        message.append("| ").append(entry.getKey().getKey()).append(" ").append(entry.getValue()).append(" ");
                    }

                    sender.sendMessage(message.toString());
                }
            }
        }

        if (isInventoryEmpty) {
            sender.sendMessage(ChatColor.YELLOW + "It seems that this poor man has nothing in inventory!");
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");

        return true;
    }
}
