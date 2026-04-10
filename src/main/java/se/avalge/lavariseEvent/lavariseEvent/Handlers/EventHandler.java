package se.avalge.lavariseEvent.lavariseEvent.Handlers;

import org.bukkit.Bukkit;
import se.avalge.lavariseEvent.lavariseEvent.Commands.MenuCommand;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;
import se.avalge.lavariseEvent.lavariseEvent.Game.GameEvents;
import se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers.Grace;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

public class EventHandler {

    private final LavariseEvent plugin;
    private final Game game;

    public EventHandler(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new MenuCommand(game), plugin);
        Bukkit.getPluginManager().registerEvents(game.getGameEvents(), plugin);
        Bukkit.getPluginManager().registerEvents(game.getScoreboardSidebar(), plugin);
        Bukkit.getPluginManager().registerEvents(game.getGrace(), plugin);
    }
}
