package cc.akashic.insight.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashMap;


public final class ListNameEditor {
    private static final HashMap<String, String[]> playerListNameMap = new HashMap<>();
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();

    public static void setPlayerListNamePrefix(Player player, String prefix) {
        String playerName = player.getName();
        String[] extraList = playerListNameMap.get(playerName);

        if (extraList == null) {
            extraList = new String[]{prefix, ""};
        } else {
            extraList[0] = prefix;
        }

        playerListNameMap.put(playerName, extraList);
        player.setPlayerListName(legacyComponentSerializer.serialize(Component.text(extraList[0], NamedTextColor.YELLOW).append(Component.text(playerName, NamedTextColor.WHITE)).append(Component.text(extraList[1], NamedTextColor.LIGHT_PURPLE))));
    }

    public static void setPlayerListNameSuffix(Player player, String suffix) {
        String playerName = player.getName();
        String[] extraList = playerListNameMap.get(playerName);

        if (extraList == null) {
            extraList = new String[]{"", suffix};
        } else {
            extraList[1] = suffix;
        }

        playerListNameMap.put(playerName, extraList);
        player.setPlayerListName(legacyComponentSerializer.serialize(Component.text(extraList[0], NamedTextColor.YELLOW).append(Component.text(playerName, NamedTextColor.WHITE)).append(Component.text(extraList[1], NamedTextColor.LIGHT_PURPLE))));
    }
}
