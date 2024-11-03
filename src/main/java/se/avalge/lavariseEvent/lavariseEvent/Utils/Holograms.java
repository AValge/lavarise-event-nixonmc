package se.avalge.lavariseEvent.lavariseEvent.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Holograms {

    private final JavaPlugin plugin;
    private final List<TextDisplay> lavaHolograms;
    private final World world;

    public Holograms(JavaPlugin plugin) {
        this.plugin = plugin;
        this.lavaHolograms = new ArrayList<>();
        this.world = Bukkit.getWorld("world");
    }

    public void displayLavaHolograms() {
        if (world == null) {
            plugin.getLogger().severe("World 'world' not found!");
            return;
        }

        String welcomeString = ChatColor.YELLOW + ChatColor.BOLD.toString() + "≫ Welcome to Lava Rising! ≪";
        addHologram(new Location(world, -22.500, 88.350, -16.500), welcomeString);
        addHologram(new Location(world, -14.500, 88.800, -0.500), welcomeString);
        addHologram(new Location(world, -14.500, 88.800, -33.500), welcomeString);
    }

    private void addHologram(Location location, String text) {
        TextDisplay hologram = (TextDisplay) world.spawnEntity(location, EntityType.TEXT_DISPLAY);
        hologram.setText(text);
        hologram.setShadowed(true);
        hologram.setBillboard(Display.Billboard.VERTICAL);
        hologram.setViewRange(100);
        hologram.setBrightness(new Display.Brightness(14, 14));
        hologram.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));
        lavaHolograms.add(hologram);
    }

    public void removeLavaHolograms() {
        for (TextDisplay hologram : lavaHolograms) {
            if (hologram != null && !hologram.isDead()) {
                hologram.remove();
            }
        }
        lavaHolograms.clear();
    }
}
