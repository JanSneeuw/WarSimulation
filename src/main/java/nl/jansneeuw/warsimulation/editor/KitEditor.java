package nl.jansneeuw.warsimulation.editor;

import nl.jansneeuw.warsimulation.Warsimulation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KitEditor {
    Warsimulation plugin;
    public KitEditor(Warsimulation instance){
        plugin = instance;
    }

    private List<String> kNumber = new ArrayList<>();
    private List<ItemStack> gearStacks = new ArrayList<>();
    private List<ItemStack> inventoryStacks = new ArrayList<>();

    public  void createKit(String kitString, Player player){
        Inventory kitInventory = player.getInventory();
        int len = kitString.length();
        for (int i = 0; i < len; i += 2) {
            kNumber.add(kitString.substring(i, Math.min(len, i + 2)));
        }
        int slot = 0;
        int item = 0;
        for (String number : kNumber){
            ItemStack kitStack = decodeKitNumber(number);
            if (item == 0 || item == 1 || item == 2 || item == 3){
                gearStacks.add(kitStack);
            }else {
                inventoryStacks.add(kitStack);
                slot += 1;
            }
            item += 1;
        }
        kitInventory.setContents( inventoryStacks.toArray(new ItemStack[inventoryStacks.size()]));
        ((PlayerInventory) kitInventory).setArmorContents(gearStacks.toArray(new ItemStack[gearStacks.size()]));
    }

    private ItemStack decodeKitNumber(String kitString){
        if (kitString.equals("00")) {
            return null;
        }else {
            if (plugin.getKitStacks().get(kitString) != null) {
                ItemStack numberStack = plugin.getKitStacks().getItemStack(kitString);
                return numberStack;
            }else{
                return null;
            }
        }
    }


    public String inventoryToKitString(Inventory inventory){
        StringBuilder kitStringBuilder = new StringBuilder();
        int slot = 0;
        for (ItemStack stack : inventory.getContents()){
            if (stack == null){
                kitStringBuilder.append("00");
            }else {
                for (String number : plugin.getKitStacks().getKeys(false)) {
                    if (stack.toString().replace("Material.", "").equalsIgnoreCase(plugin.getKitStacks().getString(number + ".Material"))) {
                        kitStringBuilder.append(number);
                    }
                }
            }
            slot += 1;
        }
        return kitStringBuilder.toString();

    }

    public String playerInventoryToKitString(PlayerInventory inventory){
        StringBuilder kitStringBuilder = new StringBuilder();
        int slot = 0;
        for (ItemStack gearStacks : inventory.getArmorContents()){
            if (gearStacks == null){
                kitStringBuilder.append("00");
            }else {
                boolean found = false;
                found = isFound(kitStringBuilder, gearStacks, found);
                if (!found){
                    itemStackToInt(gearStacks);
                    boolean f = false;
                    boolean add = isFound(kitStringBuilder, gearStacks, f);
                }
            }
            slot += 1;
        }
        for (ItemStack stack : inventory.getContents()){
            if (stack == null){
                kitStringBuilder.append("00");
            }else {

                boolean found = false;
                found = isFound(kitStringBuilder, stack, found);
                if (!found){
                    itemStackToInt(stack);
                    boolean f = false;
                    boolean add = isFound(kitStringBuilder, stack, f);
                }
            }
            slot += 1;
        }
        return kitStringBuilder.toString();
    }

    private boolean isFound(StringBuilder kitStringBuilder, ItemStack gearStacks, boolean found) {
        for (String number : plugin.getKitStacks().getKeys(false)) {
            if (!found) {
                ItemStack iS = plugin.getKitStacks().getItemStack(number);
                if (gearStacks.isSimilar(iS)){
                    found = true;
                    kitStringBuilder.append(number);
                }
            }
        }
        return found;
    }

    public void itemStackToInt(ItemStack itemStack){
        boolean exists = false;
        if (!new File(plugin.getDataFolder() + File.separator + "kiteditor" + File.separator + "KitStacks.yml").exists()){
            try {
                boolean create = new File(plugin.getDataFolder() + File.separator + "kiteditor" + File.separator + "KitStacks.yml").createNewFile();
                if (create){
                    System.out.println("KitEditor file created!");
                }
            } catch (IOException e) {
                System.out.println(e.getCause() + ", still trying to create file.");
            }
        }
        for (String section : plugin.getKitStacks().getKeys(false)){
            if (plugin.getKitStacks().getItemStack(section).isSimilar(itemStack)){
                exists = true;
            }
        }
        if (!exists){

            boolean found = false;
            for (Integer ix = 10; ix <= 99; ix++){
                String countString = ix.toString();
                if (!found) {
                    if (plugin.getKitStacks().getItemStack(countString) == null){
                        found = true;
                        plugin.getKitStacks().set(countString, itemStack);
                        plugin.saveKitStacks();
                    }
                }
            }
        }
    }
    /*
    TEST FAIL
    public Inventory getEditor(String kitString){
        Inventory inventory = Bukkit.createInventory(null, 45, "KitEditor");
        int len = kitString.length();
        for (int i = 0; i < len; i += 2) {
            kNumber.add(kitString.substring(i, Math.min(len, i + 2)));
        }
        int slot = 0;
        int item = 0;
        for (String number : kNumber){
            ItemStack kitStack = decodeKitNumber(number);
            if (item == 0 || item == 1 || item == 2 || item == 3){

            }else {
                inventory.setItem(slot + addToSlot(slot), kitStack);
                slot += 1;
            }
            item += 1;
        }

        return inventory;
    }
    */
    private Inventory getEditor(String kitString){
        Inventory kitEditor = Bukkit.createInventory(null, 27, "KitEditor");

        ItemStack ready = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta readyMeta = ready.getItemMeta();
        readyMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "KLAAR");
        ready.setItemMeta(readyMeta);

        ItemStack stop = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta stopMeta = stop.getItemMeta();
        stopMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "STOP");
        stop.setItemMeta(stopMeta);

        kitEditor.setItem(12, ready);
        kitEditor.setItem(14, stop);

        return kitEditor;
    }

    public void openEditor(Player player, String kitString){
        Inventory kitInventory = player.getInventory();
        int len = kitString.length();
        for (int i = 0; i < len; i += 2) {
            kNumber.add(kitString.substring(i, Math.min(len, i + 2)));
        }
        int slot = 0;
        int item = 0;
        for (String number : kNumber){
            ItemStack kitStack = decodeKitNumber(number);
            if (item > 3){
                inventoryStacks.add(kitStack);
                slot += 1;
            }
            item += 1;
        }
        kitInventory.setContents((ItemStack[]) inventoryStacks.toArray(new ItemStack[inventoryStacks.size()]));

        Inventory editor = getEditor(kitString);
        player.openInventory(editor);
    }

    private int addToSlot(Integer slot){
        Integer result = 0;
        if (slot <= 8){
            result = 45;
        }else if (slot <= 17){
            result = 27;
        }else if (slot <= 26){
            result = 9;
        }else if (slot <= 35){
            result = -9;
        }
        return result;
    }
}
