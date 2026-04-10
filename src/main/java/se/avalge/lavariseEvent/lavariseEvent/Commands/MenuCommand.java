package se.avalge.lavariseEvent.lavariseEvent.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import se.avalge.lavariseEvent.lavariseEvent.Game.Game;

public class MenuCommand implements CommandExecutor, Listener {

    private final Game game;

    public MenuCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory lavaisrisingmenu = Bukkit.createInventory(player, 27, ChatColor.RED + "Lava Rising Menu");

            ItemStack startButton = new ItemStack(Material.LIME_DYE);
            ItemMeta meta1 = startButton.getItemMeta();
            assert meta1 != null;
            meta1.setDisplayName(ChatColor.GREEN + "Start Game");
            meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            startButton.setItemMeta(meta1);
            lavaisrisingmenu.setItem(12, startButton);

            ItemStack stopButton = new ItemStack(Material.RED_DYE);
            ItemMeta meta2 = stopButton.getItemMeta();
            assert meta2 != null;
            meta2.setDisplayName(ChatColor.RED + "Force Stop");
            meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            stopButton.setItemMeta(meta2);
            lavaisrisingmenu.setItem(14, stopButton);

            player.openInventory(lavaisrisingmenu);
        }
        return false;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (ChatColor.translateAlternateColorCodes('&', event.getView().getTitle()).equals(ChatColor.RED + "Lava Rising Menu")
                && event.getCurrentItem() != null) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if (!player.hasPermission("lavarise.menu")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this.");
                player.closeInventory();
                return;
            }

            switch (event.getRawSlot()) {
                case 12:
                    game.startGame();
                    player.closeInventory();
                    break;
                case 14:
                    game.forceStop();
                    player.closeInventory();
                    break;
                default:
                    break;
            }
        }
    }
}
