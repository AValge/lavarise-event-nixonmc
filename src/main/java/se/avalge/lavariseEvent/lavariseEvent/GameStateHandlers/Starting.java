package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Map.GameBorder;
import se.avalge.lavariseEvent.lavariseEvent.Utils.Locations;

import java.util.ArrayList;
import java.util.List;

public class Starting {

    private final LavariseEvent plugin;
    private final Game game;
    private final List<Player> alivePlayers;

    public Starting(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
        this.alivePlayers = new ArrayList<>();
    }

    // Method to start the game
    public void gameStart() {
        startSequence1();
    }

    // Start sequence 1
    public void startSequence1() {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.sendMessage(ChatColor.GREEN + "Starting game...");
        }

        Bukkit.getScheduler().runTask(plugin, () -> new BukkitRunnable() {
            int secondsLeft = 3;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendMessage(ChatColor.GREEN + "Starting in " + secondsLeft);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    }
                    secondsLeft--;
                } else {
                    this.cancel();
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendMessage(ChatColor.GREEN + "Teleporting...");
                        onlinePlayers.teleport(Locations.LAVA_RISING_LOBBY);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        onlinePlayers.setFoodLevel(20);
                        onlinePlayers.setExp(0);
                        onlinePlayers.getInventory().clear();
                        onlinePlayers.setGameMode(GameMode.ADVENTURE);
                        onlinePlayers.setPlayerListName(ChatColor.GREEN + onlinePlayers.getName());
                    }
                    startSequence2();
                }
            }
        }.runTaskTimer(plugin, 0, 20));
    }

    // Start sequence 2
    public void startSequence2() {
        Bukkit.getScheduler().runTask(plugin, () -> new BukkitRunnable() {
            int secondsLeft = 17;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    secondsLeft--;
                } else {
                    this.cancel();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvgamerule doImmediateRespawn true lavarising");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvgamerule doDaylightCycle false lavarising");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvgamerule doWeatherCycle false lavarising");
                    startSequence3();
                }
            }
        }.runTaskTimer(plugin, 0, 20));
    }

    // Start sequence 3
    public void startSequence3() {
        Location randomLocation = game.getMapManager().getRandomLocation();

        if (randomLocation == null) {
            plugin.getLogger().severe("Cannot start game: no locations configured.");
            game.forceStop();
            return;
        }

        GameBorder.setWorldBorder(randomLocation);

        Bukkit.getScheduler().runTask(plugin, () -> new BukkitRunnable() {
            int secondsLeft = 3;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    String startTitle = ChatColor.GREEN.toString() + secondsLeft;
                    String startSubtitle = " ";

                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendTitle(startTitle, startSubtitle, 10, 30, 5);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    }
                    secondsLeft--;
                } else {
                    this.cancel();
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.teleport(randomLocation);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        onlinePlayers.setHealth(20);
                        onlinePlayers.setFoodLevel(20);
                        onlinePlayers.setExp(0);
                        onlinePlayers.getInventory().clear();
                        onlinePlayers.setGameMode(GameMode.ADVENTURE);
                        alivePlayers.add(onlinePlayers);
                    }
                    game.startPreGracePeriod();
                }
            }
        }.runTaskTimer(plugin, 0, 20));
    }
    public List<Player> getAlivePlayers() {
        return alivePlayers;
    }
}
