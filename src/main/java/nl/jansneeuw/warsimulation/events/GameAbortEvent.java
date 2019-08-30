package nl.jansneeuw.warsimulation.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameAbortedEvent;
import nl.jansneeuw.warsimulation.methods.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameAbortEvent implements Listener {
    Warsimulation plugin;
    public GameAbortEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void onAbort(GameAbortedEvent event){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (!player.getUniqueId().toString().equalsIgnoreCase(event.getPlayer().getUniqueId().toString())){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Game-Aborted-Message").replace("%PLAYER%", event.getPlayer().getName())));

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(plugin.getConfig().getString("FallBackServer"));
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
        }
        Player player = event.getPlayer();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(plugin.getConfig().getString("FallBackServer"));
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

        plugin.getServer().setWhitelist(true);

        if (plugin.getServer().unloadWorld(plugin.getServer().getWorld(plugin.getConfig().getString("World-Name")), false)){
            plugin.getLogger().info("Game map unloaded!");
        }else{
            plugin.getLogger().severe("Couldn't unload game map!");
        }

        plugin.getServer().createWorld(new WorldCreator(plugin.getConfig().getString("World-Name")));

        plugin.getServer().setWhitelist(false);

        GameUtils utils = new GameUtils(plugin);
        utils.restartGame();

        plugin.tasks.get("Game").cancel();
    }
}
