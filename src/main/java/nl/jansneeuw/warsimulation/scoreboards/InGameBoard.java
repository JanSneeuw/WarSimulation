package nl.jansneeuw.warsimulation.scoreboards;

import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class InGameBoard {
    public Team Blue;
    public Team Red;
    Warsimulation plugin;
    public InGameBoard(Warsimulation instance){

        this.plugin = instance;
    }

    public Scoreboard inGameBoard(String team, int Kills){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("WarSimulation", "dummy");
        objective.setDisplayName("WarSimulation");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score teamScore = objective.getScore(ChatColor.RED + "Team: ");
        Score teamScoreR = objective.getScore(ChatColor.WHITE + team);

        Score killsScore = objective.getScore(ChatColor.RED + "Kills: ");
        Score killsScoreR = objective.getScore(ChatColor.WHITE + String.valueOf(Kills));

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
        Score blueRemainR = objective.getScore(ChatColor.WHITE + "" + teamB.toString() + " alive");

        Score redRemain = objective.getScore(ChatColor.RED + "Team red: ");
        Score redRemainR = objective.getScore(ChatColor.WHITE + teamR.toString() + " alive");

        Score empty = objective.getScore(ChatColor.BLACK + " ");
        Score empty2 = objective.getScore(ChatColor.GRAY + "    ");
        Score empty3 = objective.getScore(ChatColor.YELLOW + "    ");


        teamScore.setScore(20);
        teamScoreR.setScore(19);
        empty.setScore(18);
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

        for (Player players : plugin.teamStringList.keySet()){
            scoreboard.getTeam(plugin.teamStringList.get(players)).addEntry(players.getName());
        }

        return scoreboard;
    }

}
