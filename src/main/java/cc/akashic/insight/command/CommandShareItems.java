package cc.akashic.insight.command;

import cc.akashic.insight.Insight;
import cc.akashic.insight.utils.ItemsViewer;
import cc.akashic.insight.utils.RandomString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;


public final class CommandShareItems implements CommandExecutor {
    private static final ArrayList<ItemStack[]> protectedItemStacksList = new ArrayList<>();
    private static final ArrayList<String> protectedRandomStringList = new ArrayList<>();

    private static void createDelayedDeleteTask(ItemStack[] items, String commandRandomString) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> {
            protectedItemStacksList.remove(items);
            protectedRandomStringList.remove(commandRandomString);
        }, 12000);
    }

    private static void printShareText(String commandRandomString) {
        Bukkit.broadcast(Component.text(ChatColor.GREEN + "[Click here]").clickEvent(ClickEvent.runCommand("/shareitems view " + commandRandomString)).append(Component.text(" to view.")));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only run in game!");

            return true;
        }

        ItemStack[] items; // Items to share/view.

        if (args.length == 0) { // No args means sharing the inventory of the sender.
            items = player.getInventory().getContents();
            String commandRandomString = RandomString.getRandomString(8); // Generate random string for /shareitems view ... command.

            // Add the items and random string into list.
            protectedItemStacksList.add(items);
            protectedRandomStringList.add(commandRandomString);

            createDelayedDeleteTask(items, commandRandomString); // Delete them 12000 ticks later. (10 min)

            // Send message to all players.
            Bukkit.broadcast(Component.text(org.bukkit.ChatColor.AQUA + "Player " + org.bukkit.ChatColor.DARK_PURPLE + player.getName() + org.bukkit.ChatColor.AQUA + " shared his inventory!"));

            printShareText(commandRandomString); // Send the clickable text.
        } else {
            switch (args[0]) {
                case "hand" -> {
                    if (args.length > 1) { // "/shareitems hand" means sharing the item in main hand of the sender.
                        return false;
                    }

                    items = new ItemStack[1];
                    items[0] = player.getInventory().getItemInMainHand();
                    String commandRandomString = RandomString.getRandomString(8);

                    protectedItemStacksList.add(items);
                    protectedRandomStringList.add(commandRandomString);

                    createDelayedDeleteTask(items, commandRandomString);

                    Bukkit.broadcast(Component.text(org.bukkit.ChatColor.AQUA + "Player " + player.getName() + " shared his items in hand!"));

                    printShareText(commandRandomString);
                }
                case "view" -> {
                    if (args.length != 2) { // "/shareitems view xxxxxxxx" means viewing a previous share. This command issued when a player clicked the clickable text.
                        return false;
                    }

                    // Try to find the index of the items.
                    int index = protectedRandomStringList.indexOf(args[1]);
                    if (index == -1) {
                        sender.sendMessage("Share has expired!"); // After 10 min, the items and random string will be removed from the list.
                        return true;
                    }

                    // Get the items and use GUI to share.
                    items = protectedItemStacksList.get(index);
                    ItemsViewer.guiToPlayer(items, (Player) sender, 54, "Shared Items", 1200L);

                    return true;
                }
                default -> {
                    sender.sendMessage("Error input!");
                    return false;
                }
            }
        }

        ItemsViewer.printToConsole(items);

        return true;
    }
}
