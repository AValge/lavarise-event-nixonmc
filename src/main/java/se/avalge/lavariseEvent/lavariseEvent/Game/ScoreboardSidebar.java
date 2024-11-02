package se.avalge.lavariseEvent.lavariseEvent.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import se.avalge.lavariseEvent.lavariseEvent.LavariseEvent;

import java.util.HashSet;
import java.util.Set;


public class ScoreboardSidebar implements Listener {

    private final LavariseEvent plugin;
    private final Game game;
    private int timeRemaining = 421;
    private final Set<Player> playersWithSidebar = new HashSet<>();
    private boolean pvpMessageSent = false;

    public ScoreboardSidebar(LavariseEvent plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public void startSidebarUpdateTask() {
        BukkitTask updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (timeRemaining > 0 && !game.getGameEvents().isPvPEnabled()) {
                timeRemaining--;
            } else if (timeRemaining == 0 && !pvpMessageSent) {
                game.getGameEvents().setPvPEnabled(true);
                sendPvPEnabledMessage();
                pvpMessageSent = true;
            }
            updateAllSidebars();
        }, 0L, 20L);
    }

    private void sendPvPEnabledMessage() {
        String startTitle = ChatColor.RED + ChatColor.BOLD.toString() + "LAVA RISING";
        String startSubtitle = ChatColor.YELLOW + "PvP Has been enabled!";
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.sendTitle(startTitle, startSubtitle, 15, 80, 15);
            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
        }
    }

    private void updateAllSidebars() {
        for (Player player : playersWithSidebar) {
            updateSidebar(player);
        }
    }

    public void resetPvPTimer() {
        this.timeRemaining = 421;
        this.pvpMessageSent = false;
    }

    private void applySidebar(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("playerboard", "dummy",
                ChatColor.YELLOW + ChatColor.BOLD.toString() + "≪ Lavarising Event ≫");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        resetPvPTimer();

        player.setScoreboard(board);
        playersWithSidebar.add(player);

        updateSidebar(player);
    }

    public String getPvPTimeFormatted() {
        if (game.getGameEvents().isPvPEnabled()) {
            return ChatColor.GREEN + "PvP Enabled";
        } else {
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            return String.format("%dm %01ds", minutes, seconds);
        }
    }

    private void updateSidebar(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("playerboard");

        if (objective == null) return;

        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }

        objective.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "< Lavarising >");

        objective.getScore(" ").setScore(8);
        objective.getScore(ChatColor.RED + "PvP Enables in: ").setScore(7);
        objective.getScore(ChatColor.YELLOW + getPvPTimeFormatted()).setScore(6);
        objective.getScore("  ").setScore(5);
        objective.getScore(ChatColor.RED + "Lava Position:").setScore(4);
        objective.getScore(ChatColor.YELLOW + "Y: " + game.getLava().getCurrentLavaY() + " / 310").setScore(3);
        objective.getScore("   ").setScore(2);
        objective.getScore(ChatColor.RED + "Teaming is not").setScore(1);
        objective.getScore(ChatColor.RED + "allowed!").setScore(0);
    }

    private void resetScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        playersWithSidebar.remove(player);
    }

    @EventHandler
    public void onJoinLavaSidebar(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equals("lavarising")) {
            applySidebar(player);
        }
    }

    @EventHandler
    public void onPlayerQuitSidebar(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        resetScoreboard(player);
    }

    @EventHandler
    public void onPlayerTeleportSidebar(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getWorld().getName().equals("lavarising") &&
                !event.getTo().getWorld().getName().equals("lavarising")) {
            resetScoreboard(player);
        } else if (!event.getFrom().getWorld().getName().equals("lavarising") &&
                event.getTo().getWorld().getName().equals("lavarising")) {
            applySidebar(player);
        }
    }
}