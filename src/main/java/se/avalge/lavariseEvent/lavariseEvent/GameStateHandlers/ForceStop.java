package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Utils.Locations;

public class ForceStop {

    private final LavariseEvent plugin;
    private final Game game;

    public ForceStop(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startForceStop() {
        Bukkit.getScheduler().cancelTasks(plugin);
        game.getGameEvents().setPvPEnabled(false);

        String stopTitle = ChatColor.RED + "Lavarising has stopped!";
        String stopSubtitle = ChatColor.RED + "Game cancelled!";

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.teleport(Locations.LAVA_EVENT_SPAWN);
            onlinePlayers.setPlayerListName(ChatColor.GRAY + onlinePlayers.getName());
            onlinePlayers.sendTitle(stopTitle, stopSubtitle, 10, 70, 20);
            onlinePlayers.sendMessage(ChatColor.RED + "Lavarising has been cancelled!");
            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            onlinePlayers.setHealth(20);
            onlinePlayers.setFoodLevel(20);
            onlinePlayers.setExp(0);
            onlinePlayers.getInventory().clear();
            onlinePlayers.setGameMode(GameMode.ADVENTURE);

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

        Location randomLocation = game.getMapManager().getRandomLocation();
        game.getGameBorder().removeWorldBorder(randomLocation);
        game.getStarting().getOngoingGamelocation().clear();

        if (randomLocation != null) {
            game.getGameBorder().removeWorldBorder(randomLocation);
        } else {
            plugin.getLogger().severe("Random location is null! Unable to remove the world border.");
        }
    }
}
