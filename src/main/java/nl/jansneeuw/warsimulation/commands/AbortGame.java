package nl.jansneeuw.warsimulation.commands;

import nl.jansneeuw.warsimulation.custom_events.GameAbortedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbortGame implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("warsimulation.staff")){
                if (command.getName().equalsIgnoreCase("abort")){
                    GameAbortedEvent event = new GameAbortedEvent(player);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }

        return true;
    }
}
