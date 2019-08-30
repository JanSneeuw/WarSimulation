package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.LobbyJoinEvent;
import nl.jansneeuw.warsimulation.custom_events.SpectatorJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GLJoinEvent implements Listener {

    Warsimulation plugin;

    public GLJoinEvent(Warsimulation instance) {
        plugin = instance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (!plugin.gameStarted){
            BukkitTask wait = new BukkitRunnable(){

                @Override
                public void run() {
                    event.getPlayer().teleport(new Location(Bukkit.getWorld(plugin.getConfig().getString("Lobby-Name")), plugin.getConfig().getInt("Lobby-Spawn.X"), plugin.getConfig().getInt("Lobby-Spawn.Y"), plugin.getConfig().getInt("Lobby-Spawn.Z")));

                    LobbyJoinEvent lobbyEvent = new LobbyJoinEvent(event.getPlayer());
                    Bukkit.getPluginManager().callEvent(lobbyEvent);
                }
            }.runTaskLater(plugin, 2);

        }else{
            SpectatorJoinEvent spectatorJoinEvent = new SpectatorJoinEvent(event.getPlayer());
            Bukkit.getPluginManager().callEvent(spectatorJoinEvent);

            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            ItemStack leaveItem = new ItemStack(Material.BARRIER);
            ItemMeta leaveMeta = leaveItem.getItemMeta();
            leaveMeta.setDisplayName("Leave de game");
            leaveItem.setItemMeta(leaveMeta);
            event.getPlayer().getInventory().setItem(8, leaveItem);
            event.getPlayer().teleport(Bukkit.getWorld(plugin.getConfig().getString("World-Name")).getSpawnLocation());
            plugin.spectatorList.add(event.getPlayer());
        }
    }

}
