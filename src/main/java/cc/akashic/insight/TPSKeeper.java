package cc.akashic.insight;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PigZombieAngerEvent;

public final class TPSKeeper {
    private static Spark spark;
    private static World world;
    private static int originMonsterSpawnLimit;
    private static boolean isInTPSKeepMode = false;
    private static boolean isInForceTPSKeepMode = false;
    private static double TPSTriggerValue = 15.0;

    /**
     * Get Spark Plugin.
     *
     * @return true if succeeded. false if failed
     */
    public static boolean getSpark() {
        try {
            spark = SparkProvider.get();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Get world.
     */
    public static void getWorld() {
        world = Bukkit.getWorld("world");
    }

    /**
     * Save the original MonsterSpawnLimit value.
     */
    public static void saveOriginMonsterSpawnLimit() {
        originMonsterSpawnLimit = Bukkit.getMonsterSpawnLimit();
    }

    /**
     * Set TPSTriggerValue.
     *
     * @param TPSTriggerValue TPSTriggerValue
     */
    public static void setTPSTriggerValue(double TPSTriggerValue) {
        TPSKeeper.TPSTriggerValue = TPSTriggerValue;
    }

    /**
     * Set if force enable TPS Keep Mode.
     *
     * @param value true to force enable TPS Keep Mode
     */
    public static void setIsInForceTPSKeepMode(boolean value) {
        if (isInForceTPSKeepMode != value) {
            if (value) {
                isInTPSKeepMode = true;
                enableTPSKeepMode();
            } else {
                isInTPSKeepMode = false;
                disableTPSKeepMode();
            }

            isInForceTPSKeepMode = value;
        }
    }

    public static void enableTPSKeepMode() {
        // Set world difficulty to easy.
        world.setDifficulty(Difficulty.EASY);

        // Change game rules.
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_INSOMNIA, false);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);

        // Change monster spawn limit.
        world.setMonsterSpawnLimit(originMonsterSpawnLimit / 2);
    }

    public static void disableTPSKeepMode() {
        // Set world difficulty to hard.
        world.setDifficulty(Difficulty.HARD);

        // Change game rules.
        world.setGameRule(GameRule.DISABLE_RAIDS, false);
        world.setGameRule(GameRule.DO_INSOMNIA, true);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, true);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, true);

        // Change monster spawn limit.
        world.setMonsterSpawnLimit(originMonsterSpawnLimit);
    }

    public static void task() {
        if (isInForceTPSKeepMode) {
            return;
        }

        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        assert tps != null;

        if (tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1) < TPSTriggerValue) {
            if (!isInTPSKeepMode) {
                isInTPSKeepMode = true;

                enableTPSKeepMode();

                Bukkit.broadcastMessage(ChatColor.RED + "WARNING: LOW TPS! TPS KEEP MODE ENABLE!");
            }
        } else if (isInTPSKeepMode) {
            isInTPSKeepMode = false;

            disableTPSKeepMode();

            Bukkit.broadcastMessage(ChatColor.GREEN + "TPS KEEP MODE DISABLE!");
        }
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPigZombieAnger(PigZombieAngerEvent event) {
            if (isInTPSKeepMode) {
                event.setCancelled(true);
            }
        }
    }
}
