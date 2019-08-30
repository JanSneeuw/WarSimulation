package nl.jansneeuw.warsimulation.commands;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.editor.KitEditor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDefaultKit implements CommandExecutor {
    Warsimulation plugin;
    public SetDefaultKit(Warsimulation instance){
        plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("setdefaultkit")){
                if (player.hasPermission("warsimulation.staff")){
                    KitEditor editor = new KitEditor(plugin);
                    plugin.getConfig().set("default-kit-string", editor.playerInventoryToKitString(player.getInventory()));
                    plugin.saveConfig();
                }
            }
        }

        return true;
    }
}
