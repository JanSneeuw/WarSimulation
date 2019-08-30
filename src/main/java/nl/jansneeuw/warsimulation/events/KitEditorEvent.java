package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.editor.KitEditor;
import nl.jansneeuw.warsimulation.inventories.LobbyInventory;
import nl.jansneeuw.warsimulation.objects.Playerdata;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class KitEditorEvent implements Listener {
    Warsimulation plugin;
    public KitEditorEvent(Warsimulation instance){
        plugin = instance;
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerdata = null;
        File playerdatafile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + player.getUniqueId().toString() + ".yml");
        playerdata = YamlConfiguration.loadConfiguration(playerdatafile);

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!plugin.gameStarted) {
                if (player.getItemInHand() != null) {
                    ItemStack hand = player.getItemInHand();
                    if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().hasLore() && hand.getData().getItemType() == Material.COMPASS) {
                        if (hand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "Verander je kit") && hand.getItemMeta().getLore().contains(ChatColor.WHITE + "" + ChatColor.BOLD + "Geruik dit om je kit aan te passen!")){
                            KitEditor editor = new KitEditor(plugin);
                            editor.openEditor(player, playerdata.getString("Kit-String"));
                            plugin.editing.add(player);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEditorInventory(InventoryClickEvent event){
        if (event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            Playerdata playerdata = null;
            try {
                playerdata = new Playerdata(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LobbyInventory lobbyInventory = new LobbyInventory();
            if (event.getSlotType() != InventoryType.SlotType.OUTSIDE && event.getInventory().getTitle().equalsIgnoreCase("KitEditor")){
                if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "KLAAR")){
                    event.setCancelled(true);
                    plugin.editing.remove(player);
                    player.closeInventory();
                    KitEditor editor = new KitEditor(plugin);
                    String kitString = editor.playerInventoryToKitString(player.getInventory());
                    try {
                        playerdata.setKitString(kitString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getInventory().clear();
                    player.getInventory().setContents(lobbyInventory.lobbyInventory().getContents());
                }else if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "STOP")){
                    event.setCancelled(true);
                    plugin.editing.remove(player);
                    player.closeInventory();
                    player.getInventory().clear();
                    player.getInventory().setContents(lobbyInventory.lobbyInventory().getContents());
                }
            }
        }
    }
    @EventHandler
    public void onEditorClose(InventoryCloseEvent event){
        if (event.getInventory().getTitle().equalsIgnoreCase("KitEditor")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                if (plugin.editing.contains(player)) {
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.openInventory(event.getInventory());
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        if (plugin.editing.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}

