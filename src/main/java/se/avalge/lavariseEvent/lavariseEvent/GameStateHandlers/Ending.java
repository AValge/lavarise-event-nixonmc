package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.Utils.Locations;

public class Ending {

    private final JavaPlugin plugin;
    private final Game game;

    public Ending(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startEnding() {
        endSequence1();
        game.getGameEvents().setPvPEnabled(false);
    }

    public void endSequence1() {
        Bukkit.getScheduler().cancelTasks(plugin);
            Player winner = game.getStarting().getAlivePlayers().getFirst();
            String winnerName = winner.getName();
            winner.sendMessage(ChatColor.GREEN + "Congratulations, you won the game!");

            plugin.getLogger().warning(winnerName);

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            game.getStarting().getAlivePlayers().remove(onlinePlayers);
            String winnerTitle = ChatColor.GREEN + winnerName + " has won the game!";
            String winnerSubtitle = ChatColor.GREEN + "Good game! :)";
            onlinePlayers.sendTitle(winnerTitle, winnerSubtitle, 10, 70, 20);
            onlinePlayers.teleport(Locations.LAVA_EVENT_SPAWN);
            onlinePlayers.setPlayerListName(ChatColor.GRAY + onlinePlayers.getName());
            onlinePlayers.setHealth(20);
            onlinePlayers.setFoodLevel(20);
            onlinePlayers.setExp(0);
            onlinePlayers.getInventory().clear();
            onlinePlayers.setGameMode(GameMode.ADVENTURE);
            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            onlinePlayers.sendMessage(ChatColor.GREEN + winnerName + " has won the game, GG!");

            if (game.getGrace().getCountdownBossBar() != null) {
                game.getGrace().getCountdownBossBar().removePlayer(onlinePlayers);
            } else {
                plugin.getLogger().info("Could not find a bossbar. Plugin did not remove anything (This is not a bug)!");
            }

            if (game.getLava().getIntervalBossBar() != null) {
                game.getLava().getIntervalBossBar().removePlayer(onlinePlayers);
            } else {
                plugin.getLogger().info("Could not find a bossbar. Plugin did not remove anything (This is not a bug)!");
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            winner.teleport(Locations.LAVA_EVENT_WINNER);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                winner.teleport(Locations.LAVA_EVENT_SPAWN);
            }, 300L);
        }, 1L);

        Location randomLocation = game.getMapManager().getRandomLocation();
        game.getGameBorder().removeWorldBorder(randomLocation);
    }
}
