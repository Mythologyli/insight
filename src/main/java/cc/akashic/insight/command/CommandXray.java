package cc.akashic.insight.command;

import cc.akashic.insight.utils.ItemsViewer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CommandXray implements CommandExecutor {
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
        int inventorySize;

        switch (args[1]) {
            case "inventory" -> {
                items = player.getInventory().getContents().clone();
                inventorySize = 45;
            }
            case "enderchest" -> {
                items = player.getEnderChest().getContents().clone();
                inventorySize = 27;
            }
            case "hand" -> {
                items = new ItemStack[1];
                items[0] = player.getInventory().getItemInMainHand().clone();
                inventorySize = 9;
            }
            default -> {
                sender.sendMessage("Error input!");
                return false;
            }
        }

        sender.sendMessage(legacyComponentSerializer.serialize(Component.text("-------------------------", NamedTextColor.GREEN)));
        sender.sendMessage(legacyComponentSerializer.serialize(Component.text("Player: " + playerName, NamedTextColor.AQUA)));

        if (sender instanceof Player) {
            sender.sendMessage(legacyComponentSerializer.serialize(Component.text("Open a Xray result box.", NamedTextColor.YELLOW)));
            ItemsViewer.guiToPlayer(items, (Player) sender, inventorySize, "Xray of " + playerName + "'s Items", 1200L);
        } else {
            ItemsViewer.printToConsole(items);
        }

        sender.sendMessage(legacyComponentSerializer.serialize(Component.text("-------------------------", NamedTextColor.GREEN)));

        return true;
    }
}
