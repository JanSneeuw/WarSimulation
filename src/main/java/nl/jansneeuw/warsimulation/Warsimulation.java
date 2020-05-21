package nl.jansneeuw.warsimulation;

import nl.jansneeuw.warsimulation.commands.AbortGame;
import nl.jansneeuw.warsimulation.commands.AddItemInt;
import nl.jansneeuw.warsimulation.commands.SetDefaultKit;
import nl.jansneeuw.warsimulation.commands.SetLobbySpawn;
import nl.jansneeuw.warsimulation.events.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class Warsimulation extends JavaPlugin {
    public List<Player> lobbyList = new ArrayList<>();
    public List<Player> gameList = new ArrayList<>();
    public Map<Player, Team> teamsList = new HashMap<>();
    public Map<Player, Scoreboard> boardList = new HashMap<>();
    public Map<Player, String> teamStringList = new HashMap<>();
    public boolean gameStarted = false;
    public Map<Player, Integer> killsMap = new HashMap<>();
    public List<Player> spectatorList = new ArrayList<>();
    public boolean loose = false;
    public List<Player> editing = new ArrayList<>();
    public Map<String, BukkitTask> tasks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File playersdir = new File(getDataFolder() + File.separator + "players");
        if (!playersdir.exists()) {
            playersdir.mkdir();
        }
        if (!getConfig().getString("World-Name").equalsIgnoreCase("NONE")){
            World world = Bukkit.createWorld(new WorldCreator(getConfig().getString("World-Name")));
            if (world.isAutoSave()){
                world.setAutoSave(false);
            }
        }
        registerEvents(this, new GameAbortEvent(this), new GameEvent(this), new GLJoinEvent(this), new GLLeaveEvent(this), new JoinEvent(this), new KillEvent(this), new KitEditorEvent(this), new LobbyEvent(this), new SpectatorEvent(this), new TeamJoinEvent(this), new WinEvent(this));
        getCommand("AddItemInt").setExecutor(new AddItemInt(this));
        getCommand("SetDefaultKit").setExecutor(new SetDefaultKit(this));
        getCommand("SetLobbySpawn").setExecutor(new SetLobbySpawn(this));
        getCommand("Abort").setExecutor(new AbortGame());

        saveDefaultKitStacks();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

    }

    @Override
    public void onDisable() {

    }
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
    private FileConfiguration kitStacks = null;
    private File fKitStacks = null;
    public void loadkitStacks(){
        if (fKitStacks == null){
            fKitStacks = new File(getDataFolder() + File.separator + "kiteditor", "KitStacks.yml");
        }
        kitStacks = YamlConfiguration.loadConfiguration(fKitStacks);

        Reader defKitStackStream = null;
        try {
            defKitStackStream = new InputStreamReader(this.getResource("KitStacks.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defKitStackStream != null){
            YamlConfiguration defTargaryen = YamlConfiguration.loadConfiguration(defKitStackStream);
            kitStacks.setDefaults(defTargaryen);
        }
    }

    public void saveDefaultKitStacks(){
        if(fKitStacks == null){
            fKitStacks = new File(getDataFolder() + File.separator + "kiteditor", "KitStacks.yml");
        }
        if (!fKitStacks.exists()){
            this.saveResource("KitStacks.yml", false);
        }
    }
    public FileConfiguration getKitStacks(){
        if (kitStacks == null){
            loadkitStacks();
        }
        return kitStacks;
    }
    public void saveKitStacks(){
        if (kitStacks == null || fKitStacks == null){
            return;
        }
        try{
            getKitStacks().save(fKitStacks);
        }catch (IOException ex){
            getLogger().log(Level.SEVERE, "Kan KitStacks yaml niet maken!", ex);
        }
    }
}
