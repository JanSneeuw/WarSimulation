package nl.jansneeuw.warsimulation.commands;

import nl.jansneeuw.warsimulation.Warsimulation;
import nl.jansneeuw.warsimulation.editor.KitEditor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemInt implements CommandExecutor {
    Warsimulation plugin;
    public AddItemInt(Warsimulation instance){
        plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("additemint")){
                if (player.hasPermission("warsimulation.staff")) {
                    if (args.length == 0) {
                        if (player.getInventory().getItemInHand() != null) {
                            ItemStack handItem = player.getItemInHand();
                            KitEditor editor = new KitEditor(plugin);
                            editor.itemStackToInt(handItem);

                        }
                    }
                }
            }
        }

       return true;
    }
}
