package com.learning.roaenchants.Listeners;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;

public class MenuListener implements Listener {

    private NamespacedKey unbreakableKey;
    private NamespacedKey unplaceableKey;

    public MenuListener(NamespacedKey unbreakableKey, NamespacedKey unplaceableKey) {
        this.unbreakableKey = unbreakableKey;
        this.unplaceableKey = unplaceableKey;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle())
                .equals(ChatColor.BLACK.toString() + "RoA Custom Enchantments...")
                && e.getCurrentItem() != null) {
            // Don't allow player to take items out of inventory
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();

                switch (e.getRawSlot()) {
                    case 10: // Unbreakable

                        ItemStack unbreakable = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta unbreakableMeta = (EnchantmentStorageMeta) unbreakable.getItemMeta();
                        unbreakableMeta.addStoredEnchant(Enchantment.getByKey(unbreakableKey), 1, false);
                        unbreakable.setItemMeta(unbreakableMeta);
                        unbreakableMeta.setDisplayName(ChatColor.DARK_PURPLE + "Unbreakable I");
                        unbreakableMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from losing durability!"));
                        unbreakable.setItemMeta(unbreakableMeta);

                        player.getInventory().addItem(unbreakable);

                        player.sendMessage(ChatColor.GREEN + "Recieved 1x Unbreakable Enchant!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        break;
                    case 11: // Unplaceable

                        ItemStack unplaceable = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta unplaceableMeta = (EnchantmentStorageMeta) unplaceable.getItemMeta();
                        unplaceableMeta.addStoredEnchant(Enchantment.getByKey(unplaceableKey), 1, false);
                        unplaceable.setItemMeta(unplaceableMeta);
                        unplaceableMeta.setDisplayName(ChatColor.YELLOW + "Unplaceable I");
                        unplaceableMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from getting placed!"));
                        unplaceable.setItemMeta(unplaceableMeta);

                        player.getInventory().addItem(unplaceable);

                        player.sendMessage(ChatColor.GREEN + "Recieved 1x Unplaceable Enchant!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        break;
                    case 22: // Close Inventory

                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        return;
                    default:
                        return;
                }
            }




        }

    }


