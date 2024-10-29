package se.avalge.lavariseEvent.lavariseEvent.Utils;

public enum GameState {
    STARTING,     // Game is about to start, countdown, teleportation, sidebar, bossbar etc
    PRE_GRACE,    // Game is in it's pregrace period
    GRACE,        // Game is in graceperiod
    LAVA,         // Game is in lava stage
    ENDING,       // Game is over, handling winners, task cancelling, resetting, and teleportation back
    FORCESTOP,    // Sets gamestate to 'FORCESTOPPED' when event gets forcestopped
    LOBBY,        // Game is inactive, no one currently playing (Default gamestate on start)
}
