package nl.jansneeuw.warsimulation.events;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.custom_events.GameJoinEvent;
import nl.jansneeuw.warsimulation.custom_events.GameStartsEvent;
import nl.jansneeuw.warsimulation.custom_events.LobbyJoinEvent;
import nl.jansneeuw.warsimulation.custom_events.LobbyLeaveEvent;
import nl.jansneeuw.warsimulation.inventories.LobbyInventory;
import nl.jansneeuw.warsimulation.methods.GameUtils;
import nl.jansneeuw.warsimulation.scoreboards.LobbyBoard;
import nl.jansneeuw.warsimulation.validators.GameStartValidation;
import nl.jansneeuw.warsimulation.validators.Validator;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LobbyEvent implements Listener {

    Warsimulation plugin;

    public LobbyEvent(Warsimulation instance) {
        plugin = instance;
    }

    List<String> teamsArray = new ArrayList<>();
    public static int timer = 10;
    @EventHandler
    public void onLobbyJoin(LobbyJoinEvent event){
        if (!plugin.lobbyList.contains(event.getPlayer())){
            plugin.lobbyList.add(event.getPlayer());
        }
        if (event.getPlayer().isDead()){
            GameUtils utils = new GameUtils(plugin);
            utils.Respawn(event.getPlayer());
        }
        if (!event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)){
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        LobbyInventory lobbyInventory = new LobbyInventory();
        event.getPlayer().getInventory().setContents(lobbyInventory.lobbyInventory().getContents());
        LobbyBoard board = new LobbyBoard(plugin);
        event.getPlayer().setScoreboard(board.lobbyBoard("NONE"));
        Validator validator = new Validator().addValidation(new GameStartValidation(plugin));
        boolean passOn = validator.executeValidations();
        if (passOn) {
/*
            BukkitTask task = new BukkitRunnable() {

                @Override
                public void run() {
                    if (plugin.getConfig().getInt("Game-Minimum") <= plugin.lobbyList.size()) {
                        cancel();
                    } else {
                        teamsArray.add("Red");
                        teamsArray.add("Blue");
                        Random random = new Random();
                        for (Player players : plugin.lobbyList) {
                            String team;
                            int RedInt = 0;
                            int BlueInt = 0;
                            for (String teams : plugin.teamStringList.values()) {
                                if (teams.equalsIgnoreCase("Red")) {
                                    RedInt += 1;
                                } else if (teams.equalsIgnoreCase("Blue")) {
                                    BlueInt += 1;
                                }
                            }
                            if (!plugin.teamStringList.containsKey(players)) {
                                if (Bukkit.getOnlinePlayers().size() % 2 == 0) {
                                    if (BlueInt == Bukkit.getOnlinePlayers().size() / 2) {
                                        team = "Red";
                                        plugin.teamStringList.put(players, team);
                                    } else if (RedInt == Bukkit.getOnlinePlayers().size() / 2) {
                                        team = "Blue";
                                        plugin.teamStringList.put(players, team);
                                    } else {
                                        team = teamsArray.get(random.nextInt(teamsArray.size()));
                                        plugin.teamStringList.put(players, team);
                                    }
                                } else {
                                    if (BlueInt == (Bukkit.getOnlinePlayers().size() - 1) / 2) {
                                        team = "Red";
                                        plugin.teamStringList.put(players, team);
                                    } else if (RedInt == (Bukkit.getOnlinePlayers().size() - 1) / 2) {
                                        team = "Blue";
                                        plugin.teamStringList.put(players, team);
                                    } else {
                                        team = teamsArray.get(random.nextInt(teamsArray.size()));
                                        plugin.teamStringList.put(players, team);
                                    }
                                }
                            }
                            Location spawn;

                            GameJoinEvent gameJoinEvent = new GameJoinEvent(players);
                            Bukkit.getPluginManager().callEvent(gameJoinEvent);
                            plugin.lobbyList.remove(players);
                        }

                        GameStartsEvent gameStartsEvent = new GameStartsEvent();
                        Bukkit.getPluginManager().callEvent(gameStartsEvent);
                    }
                }
            }.runTaskLater(plugin, 200);
  */
            BukkitTask titleTask = new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (timer > 0) {
                        for (Player player : Bukkit.getOnlinePlayers()) {

                            IChatBaseComponent title = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + "The game starts in " + String.valueOf(timer) + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");
                            PacketPlayOutTitle titlePacket1 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, title);
                            PacketPlayOutTitle titlePacket2 = new PacketPlayOutTitle(0, 20, 0);

                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket1);
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket2);
                            timer -= 1;
                        }
                        } else {
                                this.cancel();
                                if (plugin.lobbyList.size() >= plugin.getConfig().getInt("Game-Minimum")) {
                                    teamsArray.add("Red");
                                    teamsArray.add("Blue");
                                    Random random = new Random();
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        String team;
                                        int RedInt = 0;
                                        int BlueInt = 0;
                                        for (String teams : plugin.teamStringList.values()) {
                                            if (teams.equalsIgnoreCase("Red")) {
                                                RedInt += 1;
                                            } else if (teams.equalsIgnoreCase("Blue")) {
                                                BlueInt += 1;
                                            }
                                        }
                                        if (!plugin.teamStringList.containsKey(players)) {
                                            if (Bukkit.getOnlinePlayers().size() % 2 == 0) {
                                                if (BlueInt == Bukkit.getOnlinePlayers().size() / 2) {
                                                    team = "Red";
                                                    plugin.teamStringList.put(players, team);
                                                } else if (RedInt == Bukkit.getOnlinePlayers().size() / 2) {
                                                    team = "Blue";
                                                    plugin.teamStringList.put(players, team);
                                                } else {
                                                    team = teamsArray.get(random.nextInt(teamsArray.size()));
                                                    plugin.teamStringList.put(players, team);
                                                }
                                            } else {
                                                if (BlueInt == (Bukkit.getOnlinePlayers().size() - 1) / 2) {
                                                    team = "Red";
                                                    plugin.teamStringList.put(players, team);
                                                } else if (RedInt == (Bukkit.getOnlinePlayers().size() - 1) / 2) {
                                                    team = "Blue";
                                                    plugin.teamStringList.put(players, team);
                                                } else {
                                                    team = teamsArray.get(random.nextInt(teamsArray.size()));
                                                    plugin.teamStringList.put(players, team);
                                                }
                                            }
                                        }
                                        Location spawn;
                                        players.sendMessage("toJoin");
                                        GameJoinEvent gameJoinEvent = new GameJoinEvent(players);
                                        Bukkit.getPluginManager().callEvent(gameJoinEvent);
                                        plugin.killsMap.put(players, 0);
                                        plugin.lobbyList.remove(players);
                                        players.closeInventory();
                                    }

                                    GameStartsEvent gameStartsEvent = new GameStartsEvent();
                                    Bukkit.getPluginManager().callEvent(gameStartsEvent);
                                }
                            }
                        }
                }.runTaskTimer(plugin, 0, 20);
            }
    }

    @EventHandler
    public void onLobbyLeave(LobbyLeaveEvent event){
        plugin.lobbyList.remove(event.getPlayer());
        plugin.gameList.remove(event.getPlayer());
    }

    public static void resetTimer(){
        timer = 10;
    }
}
