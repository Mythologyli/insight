package cc.akashic.insight.command;

import cc.akashic.insight.Log;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandInsight implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0]) {
            case "stat":
                OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

                if (args.length == 1) {
                    return false;
                }

                switch (args[1]) {
                    case "playoneminute":
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int playOneMinute = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);

                            Log.info("STAT|PLAY_ONE_MINUTE|" + offlinePlayer.getName() + "|" + playOneMinute);
                        }

                        break;

                    case "deaths":
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int deaths = offlinePlayer.getStatistic(Statistic.DEATHS);

                            Log.info("STAT|DEATHS|" + offlinePlayer.getName() + "|" + deaths);
                        }

                        break;

                    case "mobkills":
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mobKills = offlinePlayer.getStatistic(Statistic.MOB_KILLS);

                            Log.info("STAT|MOB_KILLS|" + offlinePlayer.getName() + "|" + mobKills);
                        }

                        break;

                    default:
                        return false;
                }

                break;

            default:
                return false;
        }

        return true;
    }
}
