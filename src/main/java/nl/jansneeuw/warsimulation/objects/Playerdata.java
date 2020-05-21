package nl.jansneeuw.warsimulation.objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Playerdata {

    Plugin plugin = Bukkit.getPluginManager().getPlugin("WarSimulation");
    String uuid = null;
    File playerfile = null;
    FileConfiguration playerdata = null;
    Player player = null;

    public Playerdata(Player player) throws IOException {
        this.uuid = player.getUniqueId().toString();
        if (new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").exists()) {
            playerfile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml");

            this.playerdata = YamlConfiguration.loadConfiguration(playerfile);
        }else{
            boolean file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").createNewFile();
            playerdata.set("Name", player.getName());
            playerdata.set("UUID", player.getUniqueId().toString());
            playerdata.set("Wins", 0);
            playerdata.set("Losses", 0);
            playerdata.set("Kit-String", plugin.getConfig().getString("default-kit-string"));
            playerdata.save(playerfile);

        }
        this.player = player;
    }

    public Playerdata(UUID uuid) throws IOException {
        this.uuid = uuid.toString();
        if (new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").exists()) {
            playerfile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml");

            this.playerdata = YamlConfiguration.loadConfiguration(playerfile);
        }else{
            boolean file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").createNewFile();
            playerdata.set("Name", player.getName());
            playerdata.set("UUID", player.getUniqueId().toString());
            playerdata.set("Wins", 0);
            playerdata.set("Losses", 0);
            playerdata.set("Kit-String", plugin.getConfig().getString("default-kit-string"));
            playerdata.save(playerfile);

        }
        this.player = player;
    }

    public Playerdata(String uuid) throws IOException {
        this.uuid = uuid;
        playerfile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml");
        if (new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").exists()) {
            playerfile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml");

            this.playerdata = YamlConfiguration.loadConfiguration(playerfile);
        }else{
            boolean file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid + ".yml").createNewFile();
            playerdata.set("Name", player.getName());
            playerdata.set("UUID", player.getUniqueId().toString());
            playerdata.set("Wins", 0);
            playerdata.set("Losses", 0);
            playerdata.set("Kit-String", plugin.getConfig().getString("default-kit-string"));
            playerdata.save(playerfile);

        }
        this.player = player;
    }

    public boolean exists(){
        return this.playerfile.exists();
    }

    public void create() throws IOException {
        boolean file = playerfile.createNewFile();
        playerdata.set("Name", player.getName());
        playerdata.set("UUID", player.getUniqueId().toString());
        playerdata.set("Wins", 0);
        playerdata.set("Losses", 0);
        playerdata.set("Kit-String", plugin.getConfig().getString("default-kit-string"));
        playerdata.save(playerfile);
    }

    public int getWins(){
        return this.playerdata.getInt("Wins");
    }

    public int getLosses(){
        return this.playerdata.getInt("Losses");
    }

    public void setKitString(String kitString) throws IOException {
        playerdata.set("Kit-String", kitString);
        save();
    }
    public String getKitString(){
        return playerdata.getString("Kit-String");
    }
    public void save() throws IOException {
        playerdata.save(playerfile);
    }

}
