package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.scoreboards.LobbyBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TeamJoinEvent implements Listener {
    Warsimulation plugin;
    public TeamJoinEvent(Warsimulation instance){
        plugin = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!plugin.gameStarted) {
                if (player.getInventory().getItemInHand() != null) {
                    ItemStack hand = player.getItemInHand();
                    if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().hasLore()) {
                        if (hand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Rood") && hand.getData().getItemType() == Material.REDSTONE_BLOCK) {
                            event.setCancelled(true);
                            if ((plugin.teamStringList.containsKey(player)) && plugin.teamStringList.get(player).equalsIgnoreCase("Red")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Already-In-Team-Message").replace("%TEAM%", "rood")));
                            } else {
                                if (joinTeamAllowed("Red")){
                                    plugin.teamStringList.put(player, "Red");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Message-Prefix")) + plugin.getConfig().getString("Join-Message").replace("%TEAM%", "rood"));
                                }else{
                                    player.sendMessage(plugin.getConfig().getString("Team-Currently-Full-Message").replace("%TEAM%", "Red"));
                                }
                            }
                        } else if (hand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Blauw") && hand.getData().getItemType() == Material.LAPIS_BLOCK) {
                            event.setCancelled(true);
                            if ((plugin.teamStringList.containsKey(player)) && plugin.teamStringList.get(player).equalsIgnoreCase("Blue")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Already-In-Team-Message").replace("%TEAM%", "blauw")));
                            } else {
                                if (joinTeamAllowed("Blue")) {
                                    plugin.teamStringList.put(player, "Blue");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Message-Prefix")) + plugin.getConfig().getString("Join-Message").replace("%TEAM%", "blauw"));
                                }else{
                                    player.sendMessage(plugin.getConfig().getString("Team-Currently-Full-Message").replace("%TEAM%", "Blue"));
                                }
                            }
                        }
                        if (plugin.teamStringList.containsKey(player)){
                            player.setScoreboard(new LobbyBoard(plugin).lobbyBoard(plugin.teamStringList.get(player)));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                ItemStack clicked = event.getCurrentItem();
                if (!plugin.gameStarted) {
                    if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().hasLore()) {
                        if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Rood") && clicked.getData().getItemType() == Material.REDSTONE_BLOCK) {
                            event.setCancelled(true);
                            if ((plugin.teamStringList.containsValue(player)) && plugin.teamStringList.get(player).equalsIgnoreCase("Red")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Already-In-Team-Message").replace("%TEAM%", "rood")));
                            } else {
                                plugin.teamStringList.put(player, "Red");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Message-Prefix")) + plugin.getConfig().getString("Join-Message").replace("%TEAM%", "rood"));
                            }
                        } else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Blauw") && clicked.getData().getItemType() == Material.LAPIS_BLOCK) {
                            event.setCancelled(true);
                            if ((plugin.teamStringList.containsValue(player)) && plugin.teamStringList.get(player).equalsIgnoreCase("Blue")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Already-In-Team-Message").replace("%TEAM%", "blauw")));
                            } else {
                                plugin.teamStringList.put(player, "Blue");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Message-Prefix")) + plugin.getConfig().getString("Join-Message").replace("%TEAM%", "blauw"));
                            }
                        }
                        if (plugin.teamStringList.containsKey(player)){
                            player.setScoreboard(new LobbyBoard(plugin).lobbyBoard(plugin.teamStringList.get(player)));
                        }
                    }
                }
            }
        }
    }


    private boolean joinTeamAllowed(String team){
        if (plugin.getConfig().getBoolean("teamBalancing")){
            int teamInt = 0;
            for (String teams : plugin.teamStringList.values()){
                if (teams.equalsIgnoreCase(team)){
                    teamInt += 1;

                }
            }
            if (teamInt >= (Bukkit.getOnlinePlayers().size() / (double) 2)){
                return false;
            }else{
                return true;
            }
        }else {
            return true;
        }
    }
}
