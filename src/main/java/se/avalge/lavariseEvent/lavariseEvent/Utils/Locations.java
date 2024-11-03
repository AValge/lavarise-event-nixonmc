package se.avalge.lavariseEvent.lavariseEvent.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Locations {

    public static final Location LAVA_RISING_LOBBY;
    public static final Location LAVA_EVENT_SPAWN;
    public static final Location LAVA_EVENT_WINNER;

    static {
        World eventWorld = Bukkit.getWorld("world");

        LAVA_RISING_LOBBY = new Location(eventWorld, -14.500, 107, 64.500, 180, 0);
        LAVA_EVENT_SPAWN = new Location(eventWorld, -14.500, 87, -16.500, -90, 0);
        LAVA_EVENT_WINNER = new Location(eventWorld, -6.500, 89, -16.500);
    }

}
