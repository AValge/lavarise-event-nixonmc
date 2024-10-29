package se.avalge.lavariseEvent.lavariseEvent.Game;

import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Utils.GameState;

public class GameStateManager {

    private final LavariseEvent plugin;
    private GameState currentState;

    public GameStateManager(LavariseEvent plugin) {
        this.plugin = plugin;
        this.currentState = GameState.LOBBY;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isInState(GameState state) {
        return currentState == state;
    }

    public void setGameState(GameState newState) {
        this.currentState = newState;
        onStateChange(newState);
    }

    private void onStateChange(GameState newState) {
        switch (newState) {
            case STARTING, GRACE, PRE_GRACE, LAVA, ENDING, FORCESTOP, LOBBY:
                break;
        }
    }
}