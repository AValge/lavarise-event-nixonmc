package se.avalge.lavariseEvent.lavariseEvent.Handlers;

import se.avalge.lavariseEvent.lavariseEvent.Commands.MenuCommand;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Map.MapManager;

public class CommandHandler {

    private final LavariseEvent plugin;
    private final MapManager mapManager;

    public CommandHandler(LavariseEvent plugin, MapManager mapManager) {
        this.plugin = plugin;
        this.mapManager = mapManager;
    }

    public void handleCommands() {
        Game game = new Game(plugin, mapManager);
        plugin.getCommand("lavarisingmenu").setExecutor(new MenuCommand(new Game(plugin, mapManager)));
    }
}
