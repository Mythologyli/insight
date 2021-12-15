package cc.akashic.insight.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class ListNameEditor {
    private static final HashMap<String, String[]> playerListNameMap = new HashMap<>();

    public static void setPlayerListNamePrefix(Player player, String prefix) {
        String playerName = player.getName();
        String[] extraList = playerListNameMap.get(playerName);

        if (extraList == null) {
            extraList = new String[]{prefix, ""};
        } else {
            extraList[0] = prefix;
        }

        playerListNameMap.put(playerName, extraList);
        player.setPlayerListName(extraList[0] + ChatColor.RESET + playerName + extraList[1]);
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
        player.setPlayerListName(extraList[0] + ChatColor.RESET + playerName + extraList[1]);
    }
}
