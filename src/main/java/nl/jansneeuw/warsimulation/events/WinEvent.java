package nl.jansneeuw.warsimulation.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameWonEvent;
import nl.jansneeuw.warsimulation.methods.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class WinEvent implements Listener {
    Warsimulation plugin;
    public WinEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void onWin(GameWonEvent event){


        for (Player players : Bukkit.getOnlinePlayers()){
            if (plugin.teamStringList.containsKey(players) && plugin.teamStringList.get(players).equalsIgnoreCase(event.getTeam())){
                Firework winFireWork = (Firework) players.getWorld().spawnEntity(players.getLocation(), EntityType.FIREWORK);
                FireworkMeta winFireWorkMeta = winFireWork.getFireworkMeta();
                FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.YELLOW).withFade(Color.RED).with(FireworkEffect.Type.STAR).trail(true).build();
                winFireWorkMeta.addEffect(effect);
                winFireWorkMeta.setPower(2);
                winFireWork.setFireworkMeta(winFireWorkMeta);
            }

            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(plugin.getConfig().getString("FallBackServer"));
                    players.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                }
            }.runTaskLater(plugin, 80);

        }
        plugin.getServer().setWhitelist(true);

        if (plugin.getServer().unloadWorld(plugin.getConfig().getString("World-Name"), false)){
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
