package cc.akashic.insight.command;

import cc.akashic.insight.Insight;
import cc.akashic.insight.utils.ItemsViewer;
import cc.akashic.insight.utils.RandomString;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public final class CommandShareItems implements CommandExecutor {
    private final ArrayList<ItemStack[]> protectedItemStacksList = new ArrayList<>();
    private final ArrayList<String> protectedRandomStringList = new ArrayList<>();

    private static void printShareText(String commandRandomString) {
        TextComponent textComponent = new TextComponent(net.md_5.bungee.api.ChatColor.GREEN + "[Click here]");
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/shareitems view " + commandRandomString));

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            player.spigot().sendMessage(textComponent, new TextComponent(" to view."));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack[] items;

            if (args.length == 0) {
                items = player.getInventory().getContents();
                String commandRandomString = RandomString.getRandomString(8);

                protectedItemStacksList.add(items);
                protectedRandomStringList.add(commandRandomString);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> {
                    protectedItemStacksList.remove(items);
                    protectedRandomStringList.remove(commandRandomString);
                }, 12000L);

                Bukkit.broadcastMessage(org.bukkit.ChatColor.GREEN + "=========================");
                Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Player " + player.getName() + " shared his inventory!");

                printShareText(commandRandomString);
            } else {
                switch (args[0]) {
                    case "hand" -> {
                        if (args.length > 1) {
                            return false;
                        }

                        items = new ItemStack[1];
                        items[0] = player.getInventory().getItemInMainHand();
                        String commandRandomString = RandomString.getRandomString(8);

                        protectedItemStacksList.add(items);
                        protectedRandomStringList.add(commandRandomString);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> {
                            protectedItemStacksList.remove(items);
                            protectedRandomStringList.remove(commandRandomString);
                        }, 12000L);

                        Bukkit.broadcastMessage(org.bukkit.ChatColor.GREEN + "=========================");
                        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Player " + player.getName() + " shared his items in hand!");

                        printShareText(commandRandomString);
                    }
                    case "view" -> {
                        if (args.length > 2) {
                            return false;
                        }

                        int index = protectedRandomStringList.indexOf(args[1]);
                        if (index == -1) {
                            sender.sendMessage("Share has expired!");
                            return true;
                        }

                        items = protectedItemStacksList.get(index);
                        sender.sendMessage(org.bukkit.ChatColor.YELLOW + "Open a Shared Items box.");
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

            Bukkit.broadcastMessage(org.bukkit.ChatColor.GREEN + "=========================");
        } else {
            sender.sendMessage("This command can only run in game!");
        }

        return true;
    }
}
