package nl.jansneeuw.warsimulation.scoreboards;

import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public class LobbyBoard {

    public Team Red;
    public Team Blue;

    Warsimulation plugin;
    public LobbyBoard(Warsimulation instance){
        this.plugin = instance;
    }
    public Scoreboard lobbyBoard(String currentTeam){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("WarSimulation", "dummy");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("scoreboard-title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score empty = objective.getScore(ChatColor.BLACK + "   ");
        Score team = objective.getScore(ChatColor.RED + "Team: ");
        Score teamString = objective.getScore(ChatColor.WHITE + currentTeam);

        empty.setScore(16);
        team.setScore(15);
        teamString.setScore(14);
        Blue = scoreboard.registerNewTeam("Blue");
        Red = scoreboard.registerNewTeam("Red");
        Blue.setAllowFriendlyFire(false);
        Blue.setPrefix("§1");

        Red.setAllowFriendlyFire(false);
        Red.setPrefix("§c");
        return scoreboard;
    }
}
