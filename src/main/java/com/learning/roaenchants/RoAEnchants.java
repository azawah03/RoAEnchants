package com.learning.roaenchants;

import com.learning.roaenchants.Commands.EnchantCommand;
import com.learning.roaenchants.Commands.ROAECommand;
import com.learning.roaenchants.Enchantments.DoubleJump;
import com.learning.roaenchants.Enchantments.Unbreakable;
import com.learning.roaenchants.Enchantments.Unplaceable;
import com.learning.roaenchants.Listeners.AnvilListener;
import com.learning.roaenchants.Listeners.ArmorEquip.ArmorEquipEvent;
import com.learning.roaenchants.Listeners.ArmorEquip.ArmorListener;
import com.learning.roaenchants.Listeners.ArmorEquip.DispenserArmorListener;
import com.learning.roaenchants.Listeners.MenuListener;
import com.learning.roaenchants.TabCompleter.ROAETab;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public final class RoAEnchants extends JavaPlugin implements Listener {
    // Class-Instance Variables
    Unbreakable unbreakable = new Unbreakable();
    Unplaceable unplaceable = new Unplaceable();
    DoubleJump doubleJump = new DoubleJump();

    @Override
    public void onEnable() {


        //Register Commands
        getCommand("enchants").setExecutor(new EnchantCommand(this));
        getCommand("roae").setExecutor(new ROAECommand(this, unbreakable.getKey(), unplaceable.getKey(), doubleJump.getKey()));

        //Tab Completer
        getCommand("roae").setTabCompleter(new ROAETab());

        Bukkit.getPluginManager().registerEvents(this, this);

        // Enchantments
        Bukkit.getPluginManager().registerEvents(unbreakable, this);
        Bukkit.getPluginManager().registerEvents(unplaceable, this);
        Bukkit.getPluginManager().registerEvents(doubleJump, this);
        // Enchantment Listeners
        Bukkit.getPluginManager().registerEvents(new DoubleJump(), this);
        Bukkit.getPluginManager().registerEvents(new Unplaceable(), this);
        // Other Listeners
        Bukkit.getPluginManager().registerEvents(new AnvilListener(unbreakable.getKey(), unbreakable, unplaceable.getKey(), unplaceable), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(unbreakable.getKey(), unplaceable.getKey()), this);
        // Command Listeners
        Bukkit.getPluginManager().registerEvents(new EnchantCommand(this), this);
        Bukkit.getPluginManager().registerEvents(new ROAECommand(this, unbreakable.getKey(), unplaceable.getKey(), doubleJump.getKey()), this);
        // ArmorEquip LIB
        getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
        try{
            //Better way to check for this? Only in 1.13.1+?
            Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
            getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        }catch(Exception ignored){}
        // Function calls
        registerEnchantment(unbreakable);
        registerEnchantment(unplaceable);
        registerEnchantment(doubleJump);
    }


    private void registerEnchantment(Enchantment enchantment) {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().info("FAILED TO ADD ENCHANTMENT: " + enchantment.toString() + ". TRYING AGAIN IN 20 SECONDS! NOTE: IF THIS SPAMS PLEASE CONTACT ORETHEUS!!!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    registerEnchantment(enchantment);
                }
            }.runTaskLater(this, 20 * 20);
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        ItemStack equippedArmor = e.getNewArmorPiece();
        ItemStack unequippedArmor = e.getOldArmorPiece();
        Player player = e.getPlayer();


        if (equippedArmor != null && doubleJump.isBoot(equippedArmor) && equippedArmor.containsEnchantment(doubleJump)) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.setAllowFlight(true);
                player.sendMessage("TESTING: You have 1equipped boots with the double jump enchant!");
            }
        }

        if (unequippedArmor != null && doubleJump.isBoot(unequippedArmor) && unequippedArmor.containsEnchantment(doubleJump)) {
            if (player.getGameMode() == GameMode.SURVIVAL && player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage("TESTING: You have unequipped boots with the double jump enchant!");
            }
        }
    }
}
