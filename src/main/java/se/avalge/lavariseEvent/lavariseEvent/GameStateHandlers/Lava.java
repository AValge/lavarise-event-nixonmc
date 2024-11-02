package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

public class Lava {

    private final LavariseEvent plugin;
    private BukkitTask lavaFillTask;
    private int currentLavaY;
    private final int bottomY = -63; // Define bottom as bedrock level
    private final int topY = 309;  // Define top as 300
    private final long lavaRiseInterval = 3L; // Interval for lava rising (in seconds)
    private BossBar intervalBossBar;
    private BukkitTask lavaRiseTask;
    private final Game game;

    public Lava(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.currentLavaY = bottomY;
        this.game = game;
    }

    public void startLava() {
        resetCurrentLavaY();
        lavaStartBroadcast();
        startLavaRiseCountdown();
        startRising();
    }

    public void resetCurrentLavaY() {
        this.currentLavaY = -63;
    }

    public void lavaStartBroadcast() {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            String graceTitle = ChatColor.RED + ChatColor.BOLD.toString() + "LAVA RISING";
            String graceSubtitle = ChatColor.YELLOW + "Lava has started rising!";
            onlinePlayers.sendTitle(graceTitle, graceSubtitle, 10, 125, 30);
            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
        }
    }

    public void startRising() {
        game.getLava().resetCurrentLavaY();

        this.lavaFillTask = plugin.getServer().getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (currentLavaY > topY) {
                        stopLavaRising();
                        game.getShrinkBorder().shrinkWorldborderTask();
                        return;
                    }

                    fillLavaAtY(currentLavaY);
                    currentLavaY++;
                },
                0L,
                lavaRiseInterval * 20L
        );
    }

    public void startLavaRiseCountdown() {
        if (lavaRiseTask != null) {
            lavaRiseTask.cancel();
        }

        if (intervalBossBar != null) {
            intervalBossBar.removeAll();
        } else {
            intervalBossBar = Bukkit.createBossBar(ChatColor.BOLD + "Next lava rise in: 5 seconds", BarColor.RED, BarStyle.SOLID);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals("lavarising")) {
                intervalBossBar.addPlayer(player);
            }
        }

        final int countdownSeconds = 3;
        final int totalTicks = countdownSeconds * 20;

        lavaRiseTask = new BukkitRunnable() {
            int remainingTicks = totalTicks;

            @Override
            public void run() {
                if (remainingTicks <= 0) {
                    intervalBossBar.setProgress(1.0);
                    remainingTicks = totalTicks;
                }

                int secondsLeft = (remainingTicks + 19) / 20;
                intervalBossBar.setTitle(ChatColor.BOLD + "Lava rises in: " + secondsLeft + " seconds");

                intervalBossBar.setProgress((double) remainingTicks / totalTicks);

                remainingTicks--;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void stopLavaRising() {
        if (lavaFillTask != null) {
            lavaFillTask.cancel();
        }
        if (lavaRiseTask != null) {
            lavaRiseTask.cancel();
        }
        if (intervalBossBar != null) {
            intervalBossBar.removeAll();
            intervalBossBar = null;
        }
    }

    public void fillLavaAtY(int yLevel) {
        World world = Bukkit.getWorld("lavarising");
        org.bukkit.WorldBorder worldBorder = world.getWorldBorder();

        Location borderCenter = worldBorder.getCenter();
        double borderRadius = worldBorder.getSize() / 2;

        int minX = (int) (borderCenter.getX() - borderRadius);
        int maxX = (int) (borderCenter.getX() + borderRadius);
        int minZ = (int) (borderCenter.getZ() - borderRadius);
        int maxZ = (int) (borderCenter.getZ() + borderRadius);

        new BukkitRunnable() {
            int z = minZ;
            @Override
            public void run() {
                for(int x = minX; x < maxX; x++){
                    Block block = world.getBlockAt(x, yLevel, z);
                    block.setType(Material.LAVA, false);
                }
                z++;
                if(z > maxZ){
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public BossBar getIntervalBossBar() {
        return intervalBossBar;
    }

    public int getCurrentLavaY() {
        return currentLavaY;
    }
}
