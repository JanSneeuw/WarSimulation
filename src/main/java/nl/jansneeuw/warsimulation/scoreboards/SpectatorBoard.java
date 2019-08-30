package nl.jansneeuw.warsimulation.scoreboards;

import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SpectatorBoard {

    public Team Red;
    public Team Blue;

    Warsimulation plugin;
    public SpectatorBoard(Warsimulation instance){
        this.plugin = instance;
    }
    public Scoreboard spectatorBoard(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("WarSimulation", "dummy");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("scoreboard-title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score killsScore = objective.getScore(ChatColor.RED + "Kills: ");
        Score killsScoreR = objective.getScore(ChatColor.WHITE + String.valueOf(0));

        Integer teamB = 0;
        Integer teamR = 0;
        for (Player players : plugin.teamStringList.keySet()){
            if (plugin.teamStringList.get(players).equalsIgnoreCase("Blue")){
                teamB += 1;
            }else if (plugin.teamStringList.get(players).equalsIgnoreCase("Red")){
                teamR += 1;
            }
        }

        Score blueRemain = objective.getScore(ChatColor.RED + "Team blue: ");
        Score blueRemainR = objective.getScore(ChatColor.BLACK + "" + ChatColor.WHITE + teamB.toString() + " alive");

        Score redRemain = objective.getScore(ChatColor.RED + "Team red: ");
        Score redRemainR = objective.getScore(ChatColor.WHITE + teamR.toString() + " alive");

        Score empty2 = objective.getScore(ChatColor.GRAY + "    ");
        Score empty3 = objective.getScore(ChatColor.YELLOW + "    ");

        killsScore.setScore(17);
        killsScoreR.setScore(16);
        empty2.setScore(15);
        blueRemain.setScore(14);
        blueRemainR.setScore(13);
        empty3.setScore(12);
        redRemain.setScore(11);
        redRemainR.setScore(10);
        Blue = scoreboard.registerNewTeam("Blue");
        Red = scoreboard.registerNewTeam("Red");
        Blue.setAllowFriendlyFire(false);
        Blue.setPrefix("§1");

        Red.setAllowFriendlyFire(false);
        Red.setPrefix("§c");
        return scoreboard;
    }
}
