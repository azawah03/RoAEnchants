package com.learning.roaenchants.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;


public class Unplaceable extends Enchantment implements Listener {

    public Unplaceable() {
        super(NamespacedKey.minecraft("unplaceable"));
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();

        // Check if the item has this enchantment
        if (!item.getItemMeta().hasEnchant(this)) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();

        // Make the item unbreakable
        itemMeta.setUnbreakable(true);

        // If the item is damageable, set its damage to 0
        if (itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(0);
        }

        // Update the item's metadata
        item.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand != null && itemInHand.containsEnchantment(this)) {
            e.setCancelled(true);
            player.updateInventory();
        }

        return;
    }


    public String getName() {
        return "Unplaceable";
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getStartLevel() {
        return 1;
    }

    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isCursed() {
        return false;
    }

    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }
}
