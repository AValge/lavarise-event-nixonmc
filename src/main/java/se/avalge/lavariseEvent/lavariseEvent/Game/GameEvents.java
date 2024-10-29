package se.avalge.lavariseEvent.lavariseEvent.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;
import se.avalge.lavariseEvent.lavariseEvent.Utils.GameState;
import se.avalge.lavariseEvent.lavariseEvent.Utils.Locations;

public class GameEvents implements Listener {

    private final LavariseEvent plugin;
    private final Game game;
    private boolean isPvPEnabled = false;

    public GameEvents(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void setPvPEnabled(boolean enabled) {
        this.isPvPEnabled = enabled;
    }

    public boolean isPvPEnabled() {
        return isPvPEnabled;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();

        if (!worldName.equals("lavarising")) {
            return;
        }
        Player deadPlayer = event.getEntity().getPlayer();

        game.getStarting().getAlivePlayers().remove(deadPlayer);

        deadPlayer.setGameMode(GameMode.SPECTATOR);
        deadPlayer.setPlayerListName(ChatColor.RED + deadPlayer.getName());

        String deathMessage = event.getDeathMessage();
        event.setDeathMessage(ChatColor.RED + deathMessage);

        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                deadPlayer.teleport(deadPlayer.getLastDeathLocation()), 2L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (game.getStarting().getAlivePlayers().size() == 1) {
                game.endGame();
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        String worldName = event.getPlayer().getWorld().getName();
        if (!worldName.equals("lavarising")) {
            return;
        }
        GameStateManager gameStateManager = plugin.getGame().getGameStateManager();
        if (gameStateManager.isInState(GameState.PRE_GRACE)) {
            if(event.getTo().getBlockX() > event.getFrom().getBlockX() ||
               event.getTo().getBlockX() < event.getFrom().getBlockX() ||
               event.getTo().getBlockZ() > event.getFrom().getBlockZ() ||
               event.getTo().getBlockZ() < event.getFrom().getBlockZ()) {
               event.getPlayer().teleport(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        if (!worldName.equals("lavarising") && !worldName.equals("world")) {
            return;
        }
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (!isPvPEnabled()) {
                event.setCancelled(true);
            }
        }
    }

    // Handles what happens when player join during different gamestates
    @EventHandler
    public void onPlayerLavaGameJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if (!worldName.equals("world") && !worldName.equals("lavarising")) {
            return;
        }

        GameState currentState = game.getGameStateManager().getCurrentState();

        if (currentState == GameState.STARTING) {
            game.getStarting().getAlivePlayers().add(player);
            player.setPlayerListName(ChatColor.GREEN + player.getName());
            player.teleport(Locations.LAVA_RISING_LOBBY);
            player.setGameMode(GameMode.ADVENTURE);

        } else if (currentState == GameState.PRE_GRACE || currentState == GameState.GRACE || currentState == GameState.LAVA) {
            player.setPlayerListName(ChatColor.GRAY + player.getName());
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(ChatColor.RED + "Game ongoing, wait until game is finished. Thank you for your patience!");
            player.teleport(Locations.NIXON_EVENT_SPAWN);

        } else if (currentState == GameState.ENDING || currentState == GameState.FORCESTOP || currentState == GameState.LOBBY) {
            player.teleport(Locations.NIXON_EVENT_SPAWN);
            player.getInventory().clear();
            player.setPlayerListName(ChatColor.GRAY + player.getName());
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    // Handles what happens when player quits during different gamestates
    @EventHandler
    public void onPlayerLavaGameQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String worldName = event.getPlayer().getWorld().getName();

        if (worldName.equals("world") || worldName.equals("lavarising")) {
            if (game.getGameStateManager().isInState(GameState.STARTING)) {
                game.getStarting().getAlivePlayers().remove(player);
                checkWinnerOnLeave();
            }
            if (game.getGameStateManager().isInState(GameState.PRE_GRACE)) {
                game.getStarting().getAlivePlayers().remove(player);
                checkWinnerOnLeave();
            }
            if (game.getGameStateManager().isInState(GameState.GRACE)) {
                game.getStarting().getAlivePlayers().remove(player);
                checkWinnerOnLeave();
            }
            if (game.getGameStateManager().isInState(GameState.LAVA)) {
                game.getStarting().getAlivePlayers().remove(player);
                checkWinnerOnLeave();
            }
            if (game.getGameStateManager().isInState(GameState.ENDING)) {
                game.getStarting().getAlivePlayers().remove(player);
                checkWinnerOnLeave();
            }
            if (game.getGameStateManager().isInState(GameState.FORCESTOP)) {

            }
            if (game.getGameStateManager().isInState(GameState.LOBBY)) {

            }
        }
    }

    public void checkWinnerOnLeave() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (game.getStarting().getAlivePlayers().size() == 1) {
                game.endGame();
            }
        }, 20L);
    }
}
