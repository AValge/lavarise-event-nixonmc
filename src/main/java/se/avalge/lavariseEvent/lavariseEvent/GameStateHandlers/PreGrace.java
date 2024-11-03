package se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

public class PreGrace {
    private final LavariseEvent plugin;
    private final Game game;

    public PreGrace(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startPreGrace() {
        preGraceSequence1();
    }

    // Start preGraceSequence1
    public void preGraceSequence1() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            new BukkitRunnable() {
                int secondsLeft = 1;

                @Override
                public void run() {
                    if (secondsLeft > 0) {
                        secondsLeft--;
                    } else {
                        this.cancel();
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            onlinePlayers.setGameMode(GameMode.ADVENTURE);
                        }
                        preGraceSequence2();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        });
    }

    // Start preGraceSequence2
    public void preGraceSequence2() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            new BukkitRunnable() {
                int secondsLeft = 10;
                @Override
                public void run() {
                    if (secondsLeft > 0) {
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            onlinePlayers.sendTitle(ChatColor.GREEN + "Starting in " + secondsLeft, "", 10, 20, 10);
                            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                        }
                        secondsLeft--;
                    } else {
                        this.cancel();

                        ItemStack startBread = new ItemStack(Material.BREAD);
                        ItemMeta breadItemMeta = startBread.getItemMeta();
                        assert breadItemMeta != null;
                        startBread.setItemMeta(breadItemMeta);
                        startBread.setAmount(16);

                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (game.getStarting().getAlivePlayers().contains(onlinePlayers)) {
                                onlinePlayers.sendTitle(ChatColor.GREEN + "Have fun :) ", "", 10, 50, 10);
                                onlinePlayers.setGameMode(GameMode.SURVIVAL);
                                onlinePlayers.setHealth(20);
                                onlinePlayers.setFoodLevel(20);
                                onlinePlayers.setExp(0);
                                onlinePlayers.getInventory().clear();
                                onlinePlayers.getInventory().addItem(startBread);
                                onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
                            }
                        }
                        game.getScoreboardSidebar().startSidebarUpdateTask();
                        game.startGracePeriod();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        });
    }
}
