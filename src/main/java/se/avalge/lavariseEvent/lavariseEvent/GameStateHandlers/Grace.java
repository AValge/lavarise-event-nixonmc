package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Utils.GameState;

public class Grace implements Listener {

    private final LavariseEvent plugin;
    private BossBar countdownBossBar;
    private BukkitTask preGameTask;
    private final Game game;

    public Grace(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startGrace() {
        lavaBossBarTimer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            game.startLavaRising();
        }, 120 * 20L);
    }

    public void lavaBossBarTimer() {
        countdownBossBar = Bukkit.createBossBar(ChatColor.BOLD + "Lava starts rising in: 2m 00s", BarColor.RED, BarStyle.SOLID);

        preGameTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            int countdownGrace = 120;

            @Override
            public void run() {
                if (countdownGrace <= 0) {
                    countdownBossBar.removeAll();
                    preGameTask.cancel();
                    return;
                }

                int minutes = countdownGrace / 60;
                int seconds = countdownGrace % 60;
                String formattedCountdown = String.format("%dm %01ds", minutes, seconds);

                countdownBossBar.setTitle(ChatColor.BOLD + "Lava starts rising in: " + formattedCountdown);
                countdownBossBar.setProgress((double) countdownGrace / 120.0);
                countdownGrace--;

                if (game.getGameStateManager().isInState(GameState.GRACE)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getWorld().getName().equals("lavarising")) {
                            if (!countdownBossBar.getPlayers().contains(player)) {
                                countdownBossBar.addPlayer(player);
                            }
                        } else {
                            countdownBossBar.removePlayer(player);
                        }
                    }
                } else {
                    countdownBossBar.removeAll();
                }
            }
        }, 0L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (game.getGameStateManager().isInState(GameState.GRACE) &&
                player.getWorld().getName().equals("lavarising")) {
            countdownBossBar.addPlayer(player);
        }
    }

    public BossBar getCountdownBossBar() {
        return countdownBossBar;
    }
}
