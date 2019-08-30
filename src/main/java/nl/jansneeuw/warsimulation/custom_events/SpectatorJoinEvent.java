package nl.jansneeuw.warsimulation.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpectatorJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player player;
    public SpectatorJoinEvent(Player player){
        this.player = player;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Player getPlayer(){
        return player;
    }

}
