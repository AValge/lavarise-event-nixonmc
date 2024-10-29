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

    public Holograms(JavaPlugin plugin) {
        this.plugin = plugin;
        this.lavaHolograms = new ArrayList<>();
    }

    public void DisplayLavaHolograms() {
        World world = Bukkit.getWorld("world");

        String welcomeString = ChatColor.YELLOW + ChatColor.BOLD.toString() + "≫ Welcome to Lava Rising! ≪";
        String bugInfoString = ChatColor.YELLOW + ChatColor.BOLD.toString() + "≫ Welcome to Lava Rising! ≪\n" +
                ChatColor.RED + ChatColor.BOLD + "Lava Rising may currently have bugs!\n" +
                ChatColor.RED + ChatColor.BOLD + "Please report any bug you find to staff.\n" +
                ChatColor.RED + ChatColor.BOLD + "Thank you for your patience!";

        Location hologramLoc1 = new Location(Bukkit.getWorld("world"), -22.500, 88.350, -16.500); assert world != null;
        TextDisplay informationHologram1 = (TextDisplay) world.spawnEntity((hologramLoc1), EntityType.TEXT_DISPLAY);
        informationHologram1.setText(bugInfoString);
        informationHologram1.setShadowed(true);
        informationHologram1.setBillboard(Display.Billboard.VERTICAL);
        informationHologram1.setViewRange(100);
        informationHologram1.setBrightness(new Display.Brightness(14, 14));
        informationHologram1.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));

        Location hologramLoc2 = new Location(Bukkit.getWorld("world"), -14.500, 88.800, -0.500); assert world != null;
        TextDisplay welcomeHologram1 = (TextDisplay) world.spawnEntity((hologramLoc2), EntityType.TEXT_DISPLAY);
        welcomeHologram1.setText(welcomeString);
        welcomeHologram1.setShadowed(true);
        welcomeHologram1.setBillboard(Display.Billboard.VERTICAL);
        welcomeHologram1.setViewRange(100);
        welcomeHologram1.setBrightness(new Display.Brightness(14, 14));
        welcomeHologram1.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));

        Location hologramLoc3 = new Location(Bukkit.getWorld("world"), -14.500, 88.800, -33.500); assert world != null;
        TextDisplay welcomeHologram2 = (TextDisplay) world.spawnEntity((hologramLoc3), EntityType.TEXT_DISPLAY);
        welcomeHologram2.setText(welcomeString);
        welcomeHologram2.setShadowed(true);
        welcomeHologram2.setBillboard(Display.Billboard.VERTICAL);
        welcomeHologram2.setViewRange(100);
        welcomeHologram2.setBrightness(new Display.Brightness(14, 14));
        welcomeHologram2.setBackgroundColor(org.bukkit.Color.fromARGB(0, 0, 0, 0));

        lavaHolograms.add(informationHologram1);
        lavaHolograms.add(welcomeHologram1);
        lavaHolograms.add(welcomeHologram2);
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
