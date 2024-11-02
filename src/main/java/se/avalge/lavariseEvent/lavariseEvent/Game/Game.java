package se.avalge.lavariseEvent.lavariseEvent.Game;

import se.avalge.lavariseEvent.lavariseEvent.GameStateHandlers.*;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Map.GameBorder;
import se.avalge.lavariseEvent.lavariseEvent.Map.MapManager;
import se.avalge.lavariseEvent.lavariseEvent.Map.ShrinkBorder;
import se.avalge.lavariseEvent.lavariseEvent.Utils.GameState;

public class Game {

    private final GameStateManager gameStateManager;

    private final Starting starting;
    private final PreGrace preGrace;
    private final Grace grace;
    private final Lava lava;
    private final Ending ending;
    private final ForceStop forceStop;
    private final MapManager mapManager;
    private final GameBorder gameBorder;
    private final ScoreboardSidebar scoreboardSidebar;
    private final GameEvents gameEvents;
    private final ShrinkBorder shrinkBorder;

    public Game(LavariseEvent plugin, MapManager mapManager) {
        this.mapManager = mapManager;
        starting = new Starting(plugin, this);
        gameStateManager = new GameStateManager(plugin);
        grace = new Grace(plugin, this);
        preGrace = new PreGrace(plugin, this);
        lava = new Lava(plugin, this);
        ending = new Ending(plugin, this);
        forceStop = new ForceStop(plugin, this);
        gameBorder = new GameBorder(plugin, this);
        scoreboardSidebar = new ScoreboardSidebar(plugin, this);
        gameEvents = new GameEvents(plugin, this);
        shrinkBorder = new ShrinkBorder(plugin, this);
    }

    // Run startGame method to start the game
    public void startGame() {
        gameStateManager.setGameState(GameState.STARTING);
        starting.gameStart();
    }

    // startPreGracePeriod method to start the pre-graceperiod
    public void startPreGracePeriod() {
        gameStateManager.setGameState(GameState.PRE_GRACE);
        preGrace.startPreGrace();
    }

    // Run startGracePeriod method to start the graceperiod
    public void startGracePeriod() {
        gameStateManager.setGameState(GameState.GRACE);
        grace.startGrace();
    }

    // Run startLavaRising method to start the lavarising
    public void startLavaRising() {
        gameStateManager.setGameState(GameState.LAVA);
        lava.startLava();
    }

    // Runs when winner gets picked/ending game
    public void endGame() {
        gameStateManager.setGameState(GameState.ENDING);
        ending.startEnding();
    }

    // Run forceStop method to forceStop the game
    public void forceStop() {
        gameStateManager.setGameState(GameState.FORCESTOP);
        forceStop.startForceStop();
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public Starting getStarting() {
        return starting;
    }

    public PreGrace getPreGrace() {
        return preGrace;
    }

    public Grace getGrace() {
        return grace;
    }

    public Lava getLava() {
        return lava;
    }

    public Ending getEnding() {
        return ending;
    }

    public ForceStop getForceStop() {
        return forceStop;
    }

    public GameBorder getGameBorder() {
        return gameBorder;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ScoreboardSidebar getScoreboardSidebar() {
        return scoreboardSidebar;
    }

    public GameEvents getGameEvents() {
        return gameEvents;
    }

    public ShrinkBorder getShrinkBorder() {
        return shrinkBorder;
    }
}
