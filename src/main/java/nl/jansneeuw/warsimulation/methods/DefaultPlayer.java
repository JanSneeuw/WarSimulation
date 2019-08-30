package nl.jansneeuw.warsimulation.methods;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DefaultPlayer {

    public static void setDefaultValues(YamlConfiguration configuration, File file, Player player, Configuration config) throws IOException {
        configuration.set("Name", player.getName());
        configuration.set("UUID", player.getUniqueId().toString());
        configuration.set("Wins", 0);
        configuration.set("Losses", 0);
        configuration.set("Kit-String", config.getString("default-kit-string"));
        configuration.save(file);
    }
}
