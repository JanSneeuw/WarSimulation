package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.SpectatorJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpectatorEvent implements Listener {
    Warsimulation plugin;
    public SpectatorEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void spectatorJoin(SpectatorJoinEvent event){

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
