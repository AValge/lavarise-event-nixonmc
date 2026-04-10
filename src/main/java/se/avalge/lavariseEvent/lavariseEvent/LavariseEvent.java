package se.avalge.lavariseEvent.lavariseEvent;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.Handlers.CommandHandler;
import se.avalge.lavariseEvent.lavariseEvent.Handlers.EventHandler;
import se.avalge.lavariseEvent.lavariseEvent.Map.MapManager;
import se.avalge.lavariseEvent.lavariseEvent.Utils.Holograms;

public final class LavariseEvent extends JavaPlugin {

    private MapManager mapManager;
    private Game game;
    private Holograms holograms;

    @Override
    public void onEnable() {
        initialize();
        saveDefaultConfig();
        autoSave();
    }

    @Override
    public void onDisable() {
        holograms.removeLavaHolograms();
    }

    public void initialize() {
        holograms = new Holograms(this);
        holograms.displayLavaHolograms();

        mapManager = new MapManager(this);
        mapManager.loadConfigFile();

        game = new Game(this, mapManager);

        CommandHandler commandHandler = new CommandHandler(this, mapManager, game);
        commandHandler.handleCommands();
        EventHandler eventHandler = new EventHandler(this, game);
        eventHandler.registerEvents();
    }

    public Game getGame() {
        return game;
    }

    public void autoSave() {
        String worldName = "lavarising";
        World world = Bukkit.createWorld(new WorldCreator(worldName));

        world.setAutoSave(false);
    }
}
