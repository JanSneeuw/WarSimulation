package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameLeaveEvent;
import nl.jansneeuw.warsimulation.custom_events.LobbyLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GLLeaveEvent implements Listener {

    Warsimulation plugin;

    public GLLeaveEvent(Warsimulation instance) {
        plugin = instance;
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("Lobby-Name"))){
            LobbyLeaveEvent lobbyEvent = new LobbyLeaveEvent(event.getPlayer());
            Bukkit.getPluginManager().callEvent(lobbyEvent);
        }else if (event.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("World-Name"))){
            GameLeaveEvent gameEvent = new GameLeaveEvent(event.getPlayer());
            Bukkit.getPluginManager().callEvent(gameEvent);
        }
    }
}
