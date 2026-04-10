package se.avalge.lavariseEvent.lavariseEvent.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapManager {

    private final LavariseEvent plugin;
    private final List<Location> locations = new ArrayList<>();

    public MapManager(LavariseEvent plugin) {
        this.plugin = plugin;
    }

    public void loadConfigFile() {
        ConfigurationSection locSection = plugin.getConfig().getConfigurationSection("Locations");

        if (locSection == null) {
            return;
        }

        for (String key : locSection.getKeys(false)) {
            ConfigurationSection section = locSection.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().warning("Section '" + key + "' is null");
                continue;
            }

            String worldName = section.getString("World");
            if (worldName == null || Bukkit.getWorld(worldName) == null) {
                plugin.getLogger().warning("World '" + worldName + "' is invalid or does not exist");
                continue;
            }
            World world = Bukkit.getWorld(worldName);

            double x = section.getDouble("x");
            double y = section.getDouble("y");
            double z = section.getDouble("z");
            float yaw = (float) section.getDouble("yaw");
            float pitch = (float) section.getDouble("pitch");

            Location loc = new Location(world, x, y, z, yaw, pitch);
            locations.add(loc);
        }
    }

    public Location getRandomLocation() {
        if (locations.isEmpty()) {
            plugin.getLogger().severe("No locations are configured in config.yml! Add at least one.");
            return null;
        }
        Random random = new Random();
        return locations.get(random.nextInt(locations.size()));
    }
}
