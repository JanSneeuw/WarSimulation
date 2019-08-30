package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameWonEvent;
import nl.jansneeuw.warsimulation.methods.GameUtils;
import nl.jansneeuw.warsimulation.scoreboards.InGameBoard;
import nl.jansneeuw.warsimulation.scoreboards.SpectatorBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KillEvent implements Listener {
    Warsimulation plugin;
    public KillEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (plugin.gameStarted) {
            plugin.teamStringList.remove(event.getEntity());

            if (!plugin.teamStringList.containsValue("Red")) {
                GameWonEvent gameWonEvent = new GameWonEvent("Blue");
                plugin.getServer().getPluginManager().callEvent(gameWonEvent);
            } else if (!plugin.teamStringList.containsValue("Blue")) {
                GameWonEvent gameWonEvent = new GameWonEvent("Red");
                plugin.getServer().getPluginManager().callEvent(gameWonEvent);
            }

            InGameBoard inGameBoard = new InGameBoard(plugin);
            if (event.getEntity().getKiller() instanceof Player) {
                plugin.killsMap.put(event.getEntity().getKiller(), plugin.killsMap.get(event.getEntity().getKiller()));
            }



            event.getEntity().setGameMode(GameMode.SPECTATOR);
            ItemStack leaveItem = new ItemStack(Material.BARRIER);
            ItemMeta leaveMeta = leaveItem.getItemMeta();
            leaveMeta.setDisplayName("Leave de game");
            leaveItem.setItemMeta(leaveMeta);
            event.getEntity().getInventory().setItem(8, leaveItem);

            plugin.spectatorList.add(event.getEntity());
            plugin.gameList.remove(event.getEntity());
            plugin.teamsList.remove(event.getEntity());

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (plugin.teamStringList.containsKey(players)){
                    players.setScoreboard(inGameBoard.inGameBoard(plugin.teamStringList.get(players), plugin.killsMap.get(players)));
                }else if (plugin.spectatorList.contains(players)){
                    players.setScoreboard(new SpectatorBoard(plugin).spectatorBoard());
                }
            }
            GameUtils utils = new GameUtils(plugin);
            utils.Respawn(event.getEntity());
        }
    }
}
