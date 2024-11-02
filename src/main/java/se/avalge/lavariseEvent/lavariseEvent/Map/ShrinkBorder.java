package se.avalge.lavariseEvent.lavariseEvent.Map;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;

public class ShrinkBorder {

    private final JavaPlugin plugin;
    private final Game game;

    public ShrinkBorder(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void shrinkWorldborderTask() {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.sendMessage(ChatColor.RED + "Worldborder will now start to shrink. Watch out!");
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            shrinkWorldborder(3, 200);
        }, 30L);
    }

    public void shrinkWorldborder(int size, int duration) {
        World world = Bukkit.getWorld("lavarising");
        org.bukkit.WorldBorder worldBorder = world.getWorldBorder();

        Bukkit.getScheduler().runTask(plugin, () -> new BukkitRunnable() {
            int secondsLeft = 5;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    String startTitle = ChatColor.RED.toString() + secondsLeft;
                    String startSubtitle = "";

                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendTitle(startTitle, startSubtitle, 10, 50, 10);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                    secondsLeft--;
                } else {
                    this.cancel();
                    String borderShrinkTitle = ChatColor.RED + ChatColor.BOLD.toString() + "LAVA RISING";
                    String borderShrinkSubtitle = ChatColor.YELLOW + "Border has started to shrink!";

                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendTitle(borderShrinkTitle, borderShrinkSubtitle, 10, 50, 10);
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                    }
                    worldBorder.setSize(size, duration);
                }
            }
        }.runTaskTimer(plugin, 0, 20));
    }
}
