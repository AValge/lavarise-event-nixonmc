package se.avalge.lavariseEvent.lavariseEvent.Game;

import org.bukkit.*;
import org.bukkit.boss.BossBar;
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

    // Disables movement at the start of the game
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

    // Disables PvP while graceperiod is active
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

    // Decides what happens when player join during different gamestates
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
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.ADVENTURE);
            }, 1L);

        } else if (currentState == GameState.PRE_GRACE || currentState == GameState.GRACE || currentState == GameState.LAVA) {
            player.setPlayerListName(ChatColor.RED + player.getName());
            player.sendMessage(ChatColor.RED + "Game ongoing. Wait until the game is finished!");
            player.teleport(Locations.LAVA_EVENT_SPAWN);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.ADVENTURE);
            }, 1L);
            player.getInventory().clear();

        } else if (currentState == GameState.ENDING || currentState == GameState.FORCESTOP || currentState == GameState.LOBBY) {
            player.teleport(Locations.LAVA_EVENT_SPAWN);
            player.getInventory().clear();
            player.setPlayerListName(ChatColor.GRAY + player.getName());
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.ADVENTURE);
            }, 1L);
        }
    }

    // Decides what happens when player quits during different gamestates
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
        }
    }

    public void checkWinnerOnLeave() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (game.getStarting().getAlivePlayers().size() == 1) {
                game.endGame();
            }
        }, 20L);
    }

    private void addPlayerToBossBar(BossBar bossBar, Player player) {
        if (bossBar != null) {
            bossBar.addPlayer(player);
        } else {
            plugin.getLogger().info("Could not find a bossbar. Plugin did not add player (This is not a bug!§)");
        }
    }
}
