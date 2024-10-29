package se.avalge.lavariseEvent.lavariseEvent.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

public class GameBorder {

    private final LavariseEvent plugin;
    private final Game game;

    public GameBorder(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public static void setWorldBorder(Location centerLocation) {
        World world = centerLocation.getWorld();

        if (world != null) {
            org.bukkit.WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.setCenter(centerLocation);
            worldBorder.setSize(200);
        } else {
            Bukkit.getLogger().severe("World is null for the specified location!");
        }
    }
    public void removeWorldBorder(Location location) {
        World world = location.getWorld();

        if (world != null) {
            org.bukkit.WorldBorder worldBorder = world.getWorldBorder();
            worldBorder.setSize(10000);  // Reset the border size
        } else {
            Bukkit.getLogger().severe("World is null for the provided location!");
        }
    }
}
