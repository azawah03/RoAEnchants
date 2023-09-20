package com.learning.roaenchants.Listeners;

import com.learning.roaenchants.Enchantments.Unbreakable;
import com.learning.roaenchants.Enchantments.Unplaceable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AnvilListener implements Listener {

    private NamespacedKey unbreakableKey;
    private Unbreakable unbreakableEnchant;

    private NamespacedKey unplaceableKey;
    private Unplaceable unplaceableEnchant;

    public AnvilListener(NamespacedKey unbreakableKey, Unbreakable unbreakableEnchant, NamespacedKey unplaceableKey, Unplaceable unplaceableEnchant) {
        this.unbreakableKey = unbreakableKey;
        this.unbreakableEnchant = unbreakableEnchant;

        this.unplaceableKey = unplaceableKey;
        this.unplaceableEnchant = unplaceableEnchant;
    }

//   for testing
//    @EventHandler
//    public void onJoinEvent(PlayerJoinEvent e) {
//        ItemStack pick = new ItemStack(Material.STONE_SHOVEL);
//        pick.addUnsafeEnchantment(Enchantment.getByKey(unbreakableKey), 1);
//        ItemMeta meta = pick.getItemMeta();
//        meta.setLore(Arrays.asList(ChatColor.GRAY + "Unbreakable I"));
//        pick.setItemMeta(meta);
//
//        e.getPlayer().getInventory().addItem(pick);
//    }


    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        ItemStack firstItem = e.getInventory().getItem(0);
        ItemStack secondItem = e.getInventory().getItem(1);

        // Check if one of the items is your custom enchant book
        boolean isFirstUnbreakable = isUnbreakableBook(firstItem);
        boolean isSecondUnbreakable = isUnbreakableBook(secondItem);

        //
        // CHECKING IF THE ENCHANT IS UNBREAKABLE
        //
        if (isFirstUnbreakable || isSecondUnbreakable) {
            ItemStack otherItem = isFirstUnbreakable ? secondItem : firstItem;

            if (otherItem != null && EnchantmentTarget.ALL.includes(otherItem.getType())) {
                ItemStack result = otherItem.clone();
                if (result.containsEnchantment(unbreakableEnchant)) {
                    return;
                }

                result.addUnsafeEnchantment(Enchantment.getByKey(unbreakableKey), 1);

                ItemMeta itemMeta = result.getItemMeta();
                List<String> lore = itemMeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(ChatColor.GRAY + "Unbreakable I");
                itemMeta.setLore(lore);

                result.setItemMeta(itemMeta);
                e.setResult(result);
            }
        } else {
            //
            // CHECKING IF THE ENCHANT IS UNPLACEABLE
            //
            boolean isFirstUnplaceable = isUnplaceableBook(firstItem);
            boolean isSecondUnplaceable = isUnplaceableBook(secondItem);
            if (isFirstUnplaceable || isSecondUnplaceable) {

                if (firstItem != null && secondItem != null) {
                    ItemStack result = firstItem.clone();

                    if (result != null) {
                        if (result.containsEnchantment(unplaceableEnchant)) {
                            return;
                        }

                        result.addUnsafeEnchantment(Enchantment.getByKey(unplaceableKey), 1);

                        ItemMeta itemMeta = result.getItemMeta();
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) {
                            lore = new ArrayList<>();
                        }
                        lore.add(ChatColor.GRAY + "Unplaceable I");
                        itemMeta.setLore(lore);

                        result.setItemMeta(itemMeta);
                        e.setResult(result);
                    }
                }
            }
        }
    }


    // Helper method to check if an item is your custom enchant book
    private boolean isUnbreakableBook(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.hasStoredEnchant(unbreakableEnchant);
        }
        return false;
    }

    private boolean isUnplaceableBook(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.hasStoredEnchant(unplaceableEnchant);
        }
        return false;
    }
}
