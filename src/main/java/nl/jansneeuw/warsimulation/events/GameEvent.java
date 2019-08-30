package nl.jansneeuw.warsimulation.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameJoinEvent;
import nl.jansneeuw.warsimulation.custom_events.GameLeaveEvent;
import nl.jansneeuw.warsimulation.custom_events.GameStartsEvent;
import nl.jansneeuw.warsimulation.editor.KitEditor;
import nl.jansneeuw.warsimulation.methods.GameUtils;
import nl.jansneeuw.warsimulation.objects.Playerdata;
import nl.jansneeuw.warsimulation.scoreboards.InGameBoard;
import nl.jansneeuw.warsimulation.scoreboards.PreGameBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class GameEvent implements Listener {

    Warsimulation plugin;
    public GameEvent(Warsimulation instance){
        plugin = instance;
    }
    @EventHandler
    public void onGameJoin(GameJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage("GameJoined");
        plugin.gameList.add(event.getPlayer());
        player.setGameMode(GameMode.SURVIVAL);
        Team Rood = player.getScoreboard().getTeam("Red");
        Team Blauw = player.getScoreboard().getTeam("Blue");
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
        for (Player players : plugin.teamStringList.keySet()){
            if (plugin.teamStringList.get(players).equalsIgnoreCase("Blue")){

                int x = ThreadLocalRandom.current().nextInt(plugin.getConfig().getInt("Blue-Spawn.X") - 10, plugin.getConfig().getInt("Blue-Spawn.X") + 10);
                int z = ThreadLocalRandom.current().nextInt(plugin.getConfig().getInt("Blue-Spawn.Z") - 10, plugin.getConfig().getInt("Blue-Spawn.Z") + 10);
                int y = Bukkit.getWorld(plugin.getConfig().getString("World-Name")).getHighestBlockYAt(x,z);

                Location spawn = new Location(Bukkit.getWorld(plugin.getConfig().getString("World-Name")), x,y,z);
                player.teleport(spawn);

            }else if (plugin.teamStringList.get(players).equalsIgnoreCase("Red")){

                int x = ThreadLocalRandom.current().nextInt(plugin.getConfig().getInt("Red-Spawn.X") - 10, plugin.getConfig().getInt("Red-Spawn.X") + 10);
                int z = ThreadLocalRandom.current().nextInt(plugin.getConfig().getInt("Red-Spawn.Z") - 10, plugin.getConfig().getInt("Red-Spawn.Z") + 10);
                int y = Bukkit.getWorld(plugin.getConfig().getString("World-Name")).getHighestBlockYAt(x,z);

                Location spawn = new Location(Bukkit.getWorld(plugin.getConfig().getString("World-Name")), x,y,z);
                player.teleport(spawn);

            }
        }

        KitEditor editor = new KitEditor(plugin);
        try {
            editor.createKit(new Playerdata(player).getKitString(), player);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent event){
        Player player = event.getPlayer();
        plugin.teamStringList.remove(player);
        plugin.teamsList.remove(player);
        for (Player players : plugin.teamsList.keySet()){
            InGameBoard board = new InGameBoard(plugin);
            Scoreboard scoreboard = board.inGameBoard(plugin.teamStringList.get(players), plugin.killsMap.get(players));
            players.setScoreboard(scoreboard);
        }
    }

    @EventHandler
    public void onGameStart(GameStartsEvent event){
        plugin.gameStarted = true;
        BukkitTask open = new BukkitRunnable(){

            @Override
            public void run() {
                plugin.loose = true;
                for (Player players : Bukkit.getOnlinePlayers()){
                    players.getActivePotionEffects().clear();
                    PreGameBoard board = new PreGameBoard(plugin);
                    players.setScoreboard(board.preGameBoard(plugin.teamStringList.get(players)));
                    for (Player boardPlayers : plugin.teamStringList.keySet()) {
                        if (plugin.teamStringList.get(boardPlayers).equalsIgnoreCase("Blue")) {
                            players.getScoreboard().getTeam("Blue").addEntry(boardPlayers.getName());
                            plugin.teamsList.put(boardPlayers, players.getScoreboard().getTeam("Blue"));
                        } else if (plugin.teamStringList.get(boardPlayers).equalsIgnoreCase("Red")) {
                            players.getScoreboard().getTeam("Red").addEntry(boardPlayers.getName());
                            plugin.teamsList.put(boardPlayers, players.getScoreboard().getTeam("Red"));
                        }
                    }
                }
            }
        }.runTaskLater(plugin, 100);
        BukkitTask stop = new BukkitRunnable(){

            @Override
            public void run() {
                for (Player players : Bukkit.getOnlinePlayers()){
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Message-Prefix") + plugin.getConfig().getString("Game-Limit-Reached-Message")));
            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(plugin.getConfig().getString("FallBackServer"));
                    players.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                }
            }.runTaskLater(plugin, 40);

                    players.kickPlayer("MapReload");
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
            }
        }.runTaskLater(plugin, 20*60*plugin.getConfig().getInt("Game-Limit"));
        plugin.tasks.put("Game", stop);
    }
}
