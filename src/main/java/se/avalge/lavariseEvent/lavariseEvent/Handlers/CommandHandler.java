package se.avalge.lavariseEvent.lavariseEvent.Handlers;

import se.avalge.lavariseEvent.lavariseEvent.Commands.MenuCommand;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Map.MapManager;

public class CommandHandler {

    private final LavariseEvent plugin;
    private final MapManager mapManager;
    private final Game game;

    public CommandHandler(LavariseEvent plugin, MapManager mapManager,  Game game) {
        this.plugin = plugin;
        this.mapManager = mapManager;
        this.game = game;
    }

    public void handleCommands() {
        plugin.getCommand("lavarisingmenu").setExecutor(new MenuCommand(game));
    }
}
