package cc.akashic.insight.command;

import cc.akashic.insight.Insight;
import cc.akashic.insight.Log;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            case "reload" -> {
                if (args.length != 1) {
                    return false;
                }

                Insight.instance.reloadConfig();
                Insight.config = Insight.instance.getConfig();

                sender.sendMessage("Insight reload config.yml successfully!");
            }

            case "stat" -> {
                OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

                if (args.length == 1) {
                    return false;
                }

                switch (args[1]) {
                    case "playoneminute" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int playOneMinute = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);

                            Log.info("STAT|PLAY_ONE_MINUTE|" + offlinePlayer.getName() + "|" + playOneMinute);
                        }
                    }

                    case "deaths" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int deaths = offlinePlayer.getStatistic(Statistic.DEATHS);

                            Log.info("STAT|DEATHS|" + offlinePlayer.getName() + "|" + deaths);
                        }
                    }

                    case "mobkills" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mobKills = offlinePlayer.getStatistic(Statistic.MOB_KILLS);

                            Log.info("STAT|MOB_KILLS|" + offlinePlayer.getName() + "|" + mobKills);
                        }
                    }

                    case "pickupdiamond" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int pickUpDiamond = offlinePlayer.getStatistic(Statistic.PICKUP, Material.DIAMOND);

                            Log.info("STAT|PICKUP_DIAMOND|" + offlinePlayer.getName() + "|" + pickUpDiamond);
                        }
                    }

                    case "minenetherrack" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineNetherrack = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK);

                            Log.info("STAT|MINE_NETHERRACK|" + offlinePlayer.getName() + "|" + mineNetherrack);
                        }
                    }

                    case "minedeepslate" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineDeepslate = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE);

                            Log.info("STAT|MINE_DEEPSLATE|" + offlinePlayer.getName() + "|" + mineDeepslate);
                        }
                    }

                    case "mineancientdebris" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineAncientDebris = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.ANCIENT_DEBRIS);

                            Log.info("STAT|MINE_ANCIENT_DEBRIS|" + offlinePlayer.getName() + "|" + mineAncientDebris);
                        }
                    }

                    default -> {
                        return false;
                    }
                }

            }

            default -> {
                return false;
            }
        }

        return true;
    }
}
