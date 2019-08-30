package nl.jansneeuw.warsimulation.methods;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameUtils {

    Warsimulation plugin;
    public GameUtils(Warsimulation instance){
        plugin = instance;
    }

    public void restartGame(){

        plugin.gameStarted = false;
        plugin.loose = false;

    }
    public void Respawn(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));

    }
}
