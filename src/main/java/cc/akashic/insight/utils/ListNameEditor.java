package cc.akashic.insight.utils;

import cc.akashic.insight.Insight;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.HashMap;


public final class ListNameEditor {
    private static final HashMap<String, String[]> playerListNameMap = new HashMap<>();
    private static YamlConfiguration config = null;

    private static String getPlayerLine(Player player) {
        InetSocketAddress haProxyAddress = player.getHAProxyAddress();
        if (haProxyAddress != null) {
            if (haProxyAddress.getAddress() instanceof Inet6Address) {
                return "[V6]";
            }

            if (config == null) {
                config = YamlConfiguration.loadConfiguration(new File(Insight.dataFolder + "/line.yml"));
            }

            String haProxyAddressString = haProxyAddress.getAddress().getHostAddress().replace(".", "_");
            String res = config.getString("Line." + haProxyAddressString, "?");
            return "[" + res + "]";
        } else {
            return "[?]";
        }
    }

    public static void setPlayerListNameAFKPrefix(Player player, String prefix) {
        String playerName = player.getName();
        String[] extraList = playerListNameMap.get(playerName);

        if (extraList == null) {
            extraList = new String[]{prefix, ""};
        } else {
            extraList[0] = prefix;
        }

        playerListNameMap.put(playerName, extraList);
        player.playerListName(Component.text(extraList[0], NamedTextColor.YELLOW).append(Component.text(playerName, NamedTextColor.WHITE)).append(Component.text(extraList[1], NamedTextColor.LIGHT_PURPLE)).append(Component.text(getPlayerLine(player), NamedTextColor.GRAY)));
    }

    public static void setPlayerListNameSloganSuffix(Player player, String suffix) {
        String playerName = player.getName();
        String[] extraList = playerListNameMap.get(playerName);

        if (extraList == null) {
            extraList = new String[]{"", suffix};
        } else {
            extraList[1] = suffix;
        }

        playerListNameMap.put(playerName, extraList);
        player.playerListName(Component.text(extraList[0], NamedTextColor.YELLOW).append(Component.text(playerName, NamedTextColor.WHITE)).append(Component.text(extraList[1], NamedTextColor.LIGHT_PURPLE)).append(Component.text(getPlayerLine(player), NamedTextColor.GRAY)));
    }
}
