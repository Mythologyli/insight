package cc.akashic.insight.command;

import cc.akashic.insight.Log;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.OfflinePlayer;

public class CommandInsight implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0]) {
            case "stat":
                OfflinePlayer[] offline_players = Bukkit.getOfflinePlayers();

                if (args.length == 1) {
                    return false;
                }

                switch (args[1]) {
                    case "playoneminute":
                        for (OfflinePlayer offline_player : offline_players) {
                            int play_one_minute = offline_player.getStatistic(Statistic.PLAY_ONE_MINUTE);

                            Log.info("STAT|PLAY_ONE_MINUTE|" + offline_player.getName() + "|" + play_one_minute);
                        }

                        break;

                    case "deaths":
                        for (OfflinePlayer offline_player : offline_players) {
                            int deaths = offline_player.getStatistic(Statistic.DEATHS);

                            Log.info("STAT|DEATHS|" + offline_player.getName() + "|" + deaths);
                        }

                        break;

                    case "mobkills":
                        for (OfflinePlayer offline_player : offline_players) {
                            int mob_kills = offline_player.getStatistic(Statistic.MOB_KILLS);

                            Log.info("STAT|MOB_KILLS|" + offline_player.getName() + "|" + mob_kills);
                        }

                        break;
                }

                break;
        }

        return true;
    }
}
