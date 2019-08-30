package nl.jansneeuw.warsimulation.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbyInventory {
    private List<String> blueLore = new ArrayList<>();
    private List<String> redLore = new ArrayList<>();
    private List<String> kitLore = new ArrayList<>();

    public Inventory lobbyInventory(){
        Inventory inventory = Bukkit.createInventory(null, 36);

        ItemStack roodItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta roodMeta = roodItem.getItemMeta();
        roodMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Rood");
        redLore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "Klik hierop om team rood te joinen!");
        roodMeta.setLore(redLore);
        roodItem.setItemMeta(roodMeta);

        ItemStack blueItem = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta blueMeta = blueItem.getItemMeta();
        blueMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Join Team Blauw");
        blueLore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "Klik hierop om team blauw te joinen!");
        blueMeta.setLore(blueLore);
        blueItem.setItemMeta(blueMeta);

        ItemStack kitItem = new ItemStack(Material.COMPASS);
        ItemMeta kitMeta = kitItem.getItemMeta();
        kitMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Verander je kit");
        kitLore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "Geruik dit om je kit aan te passen!");
        kitMeta.setLore(kitLore);
        kitItem.setItemMeta(kitMeta);

        inventory.setItem(3, roodItem);
        inventory.setItem(5, blueItem);
        inventory.setItem(8, kitItem);
        return inventory;
    }
}
