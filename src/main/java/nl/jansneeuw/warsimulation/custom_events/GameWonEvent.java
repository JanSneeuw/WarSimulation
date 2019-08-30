package nl.jansneeuw.warsimulation.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

public class GameWonEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    String team;
    public GameWonEvent(String team){
        this.team = team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public String getTeam(){
        return team;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
