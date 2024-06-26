package cc.akashic.insight.command;

import cc.akashic.insight.Insight;
import cc.akashic.insight.utils.ItemsViewer;
import cc.akashic.insight.utils.RandomString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

public final class CommandShareItems implements CommandExecutor {
    private static final HashMap<String, ItemStack[]> protectedShareSet = new HashMap<>();
    private static final BungeeComponentSerializer bungeeComponentSerializer = BungeeComponentSerializer.get();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only run in game!");

            return true;
        }

        ItemStack[] items; // Items to share/view.

        if (args.length == 0) { // No args means sharing the inventory of the sender.
            @Nullable ItemStack @NotNull [] player_items = player.getInventory().getContents();

            // Deep copy the array.
            items = new ItemStack[player_items.length];
            for (int i = 0, j = 0; i < player_items.length; i++) {
                if (player_items[i] != null) {
                    items[j] = player_items[i].clone();
                    j++;
                }
            }

            String commandRandomString = RandomString.getRandomString(8); // Generate random string for /shareitems view ... command.

            // Add share.
            protectedShareSet.put(commandRandomString, items);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> protectedShareSet.remove(commandRandomString), 12000); // Delete them 12000 ticks later. (10 min)

            // Send message to all players.
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player everyPlayer : players) {
                everyPlayer.spigot().sendMessage(bungeeComponentSerializer.serialize(Component.text("Player ", NamedTextColor.AQUA).append(Component.text(player.getName(), NamedTextColor.DARK_PURPLE).append(Component.text(" shared his inventory!", NamedTextColor.AQUA)))));

                // Send the clickable text.
                everyPlayer.spigot().sendMessage(bungeeComponentSerializer.serialize(Component.text("[Click here]", NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/shareitems view " + commandRandomString)).append(Component.text(" to view."))));
            }
        } else {
            switch (args[0]) {
                case "hand" -> {
                    if (args.length > 1) { // "/shareitems hand" means sharing the item in main hand of the sender.
                        return false;
                    }

                    items = new ItemStack[1];
                    items[0] = player.getInventory().getItemInMainHand().clone();
                    String commandRandomString = RandomString.getRandomString(8);

                    protectedShareSet.put(commandRandomString, items);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> protectedShareSet.remove(commandRandomString), 12000);

                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    for (Player everyPlayer : players) {
                        everyPlayer.spigot().sendMessage(bungeeComponentSerializer.serialize(Component.text("Player ", NamedTextColor.AQUA).append(Component.text(player.getName(), NamedTextColor.DARK_PURPLE).append(Component.text(" shared his items in hand!", NamedTextColor.AQUA)))));

                        // Send the clickable text.
                        everyPlayer.spigot().sendMessage(bungeeComponentSerializer.serialize(Component.text("[Click here]", NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/shareitems view " + commandRandomString)).append(Component.text(" to view."))));
                    }
                }
                case "view" -> {
                    if (args.length != 2) { // "/shareitems view xxxxxxxx" means viewing a previous share. This command issued when a player clicked the clickable text.
                        return false;
                    }

                    if (!protectedShareSet.containsKey(args[1])) {
                        sender.sendMessage("Share has expired!"); // After 10 min, the items and random string will be removed from the list.
                        return true;
                    }

                    // Get the items and use GUI to share.
                    items = protectedShareSet.get(args[1]);
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
