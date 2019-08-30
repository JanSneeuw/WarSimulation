package nl.jansneeuw.warsimulation.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LobbyJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    Player player;
    public LobbyJoinEvent(Player player){
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
