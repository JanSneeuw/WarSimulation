package nl.jansneeuw.warsimulation.commands;

import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbySpawn implements CommandExecutor {
    Warsimulation plugin;
    public SetLobbySpawn(Warsimulation instance){
        plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (sender.hasPermission("warsimulation.staff")){
                if (command.getName().equalsIgnoreCase("setLobbySpawn")){
                    plugin.getConfig().set("Lobby-World", player.getWorld().getName());
                    plugin.getConfig().set("Lobby-Spawn.X", player.getLocation().getX());
                    plugin.getConfig().set("Lobby-Spawn.Y", player.getLocation().getY());
                    plugin.getConfig().set("Lobby-Spawn.Z", player.getLocation().getZ());
                    plugin.saveConfig();

                }
            }
        }
        return true;
    }
}
