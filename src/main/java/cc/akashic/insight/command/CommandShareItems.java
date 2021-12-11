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

import java.util.Collection;
import java.util.Map;

public class CommandShareItems implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");
            Bukkit.broadcastMessage("Player " + player.getName() + " shared his inventory.");

            ItemStack[] playerItems = player.getInventory().getContents();
            boolean isInventoryEmpty = true;

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

                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    for (Player playerToSend : players) {
                        localeManager.sendMessage(playerToSend, message.toString(), material, (short) 0, enchantments);
                    }
                }
            }

            if (isInventoryEmpty) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "It seems that this poor man has nothing in inventory!");
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");

        } else {
            sender.sendMessage("This command can only run from game!");
        }

        return true;
    }
}
