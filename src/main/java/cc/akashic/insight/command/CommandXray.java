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
    protected void printItems(ItemStack[] items) {
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

    protected void printItems(ItemStack[] items, Player playerToSend) {
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage("Player not found!");
            return true;
        }

        ItemStack[] items;

        switch (args[1]) {
            case "inventory":
                items = player.getInventory().getContents();

                break;

            case "enderchest":
                items = player.getEnderChest().getContents();

                break;

            case "hand":
                items = new ItemStack[1];
                items[0] = player.getInventory().getItemInMainHand();

                break;

            default:
                sender.sendMessage("Error input!");
                return false;
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");
        sender.sendMessage(ChatColor.AQUA + "Player: " + playerName);

        if (sender instanceof Player) {
            printItems(items, (Player) sender);
        } else {
            printItems(items);
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");

        return true;
    }
}
