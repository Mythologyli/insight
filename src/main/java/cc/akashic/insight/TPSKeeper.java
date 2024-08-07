package cc.akashic.insight;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PigZombieAngerEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cc.akashic.insight.AFK.AFKPlayerSet;

public final class TPSKeeper {
    private static Spark spark;
    private static World world;
    private static int originMonsterSpawnLimit;
    private static boolean isInTPSKeepMode = false;
    private static boolean isInForceTPSKeepMode = false;
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();

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
        originMonsterSpawnLimit = Bukkit.getSpawnLimit(SpawnCategory.MONSTER);
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
        world.setSpawnLimit(SpawnCategory.MONSTER, originMonsterSpawnLimit / 2);

        // Kick all AFK players.
        for (Player player : AFKPlayerSet) {
            player.kickPlayer("Kicked by TPS Keep Mode.");
        }
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
        world.setSpawnLimit(SpawnCategory.MONSTER, originMonsterSpawnLimit);
    }

    public static void task() {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        assert tps != null;
        double tpsOneMinute = tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1);

        // Try to write TPS in tps.txt
        try {
            FileWriter fileWriter = new FileWriter(Insight.dataFolder + "/tps.txt", true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fileWriter.write(simpleDateFormat.format(new Date()) + "|" + tpsOneMinute + "\n");
            fileWriter.close();
        } catch (IOException e) {
            Log.info("Cannot write file tps.txt");
        }

        if (isInForceTPSKeepMode) {
            return;
        }

        if (tpsOneMinute < Insight.config.getDouble("TPSKeeper.tpsTriggerValue", 15.0)) {
            if (!isInTPSKeepMode) {
                isInTPSKeepMode = true;

                enableTPSKeepMode();

                Bukkit.broadcastMessage(legacyComponentSerializer.serialize(Component.text("WARNING: LOW TPS! TPS KEEP MODE ENABLE!", NamedTextColor.RED)));
            }
        } else if (isInTPSKeepMode) {
            isInTPSKeepMode = false;

            disableTPSKeepMode();

            Bukkit.broadcastMessage(legacyComponentSerializer.serialize(Component.text("TPS KEEP MODE DISABLE!", NamedTextColor.GREEN)));
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
