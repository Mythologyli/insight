package cc.akashic.insight.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class PlayerHead {
    public static ItemStack getPlayerHead(OfflinePlayer offlinePlayer) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwningPlayer(offlinePlayer);
        head.setItemMeta(headMeta);

        return head;
    }

    public static ItemStack getPlayerHead(Player player) {
        return getPlayerHead(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public static ItemStack getPlayerHead(OfflinePlayer offlinePlayer, String displayName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwningPlayer(offlinePlayer);
        headMeta.displayName(Component.text(displayName));
        head.setItemMeta(headMeta);

        return head;
    }

    public static ItemStack getPlayerHead(Player player, String displayName) {
        return getPlayerHead(Bukkit.getOfflinePlayer(player.getUniqueId()), displayName);
    }
}
