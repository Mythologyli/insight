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
import org.jetbrains.annotations.NotNull;

public final class CommandInsight implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
                if (args.length == 1) {
                    return false;
                }

                OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
                OfflinePlayer[] bestTenPlayers = new OfflinePlayer[10];

                switch (args[1]) {
                    case "playoneminute" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int playOneMinute = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.PLAY_ONE_MINUTE) < playOneMinute) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|PLAY_ONE_MINUTE|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.PLAY_ONE_MINUTE));
                        }
                    }

                    case "deaths" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int deaths = offlinePlayer.getStatistic(Statistic.DEATHS);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.DEATHS) > deaths) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|DEATHS|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.DEATHS));
                        }
                    }

                    case "mobkills" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mobKills = offlinePlayer.getStatistic(Statistic.MOB_KILLS);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.MOB_KILLS) < mobKills) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|MOB_KILLS|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.MOB_KILLS));
                        }
                    }

                    case "pickupdiamond" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int pickUpDiamond = offlinePlayer.getStatistic(Statistic.PICKUP, Material.DIAMOND);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.PICKUP, Material.DIAMOND) < pickUpDiamond) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|PICKUP_DIAMOND|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.PICKUP, Material.DIAMOND));
                        }
                    }

                    case "minenetherrack" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineNetherrack = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK) < mineNetherrack) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|MINE_NETHERRACK|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK));
                        }
                    }

                    case "minedeepslate" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineDeepslate = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE) < mineDeepslate) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|MINE_DEEPSLATE|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.DEEPSLATE));
                        }
                    }

                    case "mineancientdebris" -> {
                        for (OfflinePlayer offlinePlayer : offlinePlayers) {
                            int mineAncientDebris = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.ANCIENT_DEBRIS);

                            for (int i = 0; i < 10; i++) {
                                if (bestTenPlayers[i] == null) {
                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }

                                if (bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.ANCIENT_DEBRIS) < mineAncientDebris) {
                                    for (int j = 9; j > i; j--) {
                                        bestTenPlayers[j] = bestTenPlayers[j - 1];
                                    }

                                    bestTenPlayers[i] = offlinePlayer;
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < 10; i++) {
                            if (bestTenPlayers[i] == null) {
                                break;
                            }

                            Log.info("STAT|MINE_ANCIENT_DEBRIS|" + bestTenPlayers[i].getName() + "|" + bestTenPlayers[i].getStatistic(Statistic.MINE_BLOCK, Material.ANCIENT_DEBRIS));
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
