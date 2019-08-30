package nl.jansneeuw.warsimulation.events;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.objects.Playerdata;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class JoinEvent implements Listener {
    Warsimulation plugin;
    public JoinEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        try {
            if (!new Playerdata(event.getPlayer()).exists()){
                Playerdata playerdata = new Playerdata(player);
                try {
                    playerdata.create();
                } catch (IOException e) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NullPointerException ex){
            try {
                boolean file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File playerfile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
            YamlConfiguration playerdata = YamlConfiguration.loadConfiguration(playerfile);
            playerdata.set("Name", player.getName());
            playerdata.set("UUID", player.getUniqueId().toString());
            playerdata.set("Wins", 0);
            playerdata.set("Losses", 0);
            playerdata.set("Kit-String", plugin.getConfig().getString("default-kit-string"));
            try {
                playerdata.save(playerfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
