package com.learning.roaenchants.Commands;

import com.learning.roaenchants.RoAEnchants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EnchantCommand implements CommandExecutor, Listener {
    /** ENCHANTS:
     * - Unbreakable
     * - Unplaceable
    */

    private HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    private RoAEnchants main;

    public EnchantCommand (RoAEnchants main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            // ADD TO HASHMAP IF NOT IN IT
            PermissionAttachment attachment;
            if (!perms.containsKey(player.getUniqueId())) {
                attachment = player.addAttachment(main);
                perms.put(player.getUniqueId(), attachment);
            } else {
                attachment = perms.get(player.getUniqueId());
            }

            if (!player.hasPermission("roae.admin")) {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "RoA Enchants" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " You do not have the permissions to do this!");
                return true;
            }



            Inventory inv = Bukkit.createInventory(player, 27, ChatColor.BLACK.toString() + "RoA Custom Enchantments...");

            // UNBREAKABLE
            ItemStack unbreakable = new ItemStack(Material.BOOK);
            ItemMeta unbreakableMeta = unbreakable.getItemMeta();
            unbreakableMeta.setDisplayName(ChatColor.DARK_PURPLE + "Unbreakable I");
            unbreakableMeta.setLore(Arrays.asList(" ", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from losing durability!"));
            unbreakable.setItemMeta(unbreakableMeta);

            inv.setItem(10, unbreakable);

            // UNPLACEABLE
            ItemStack unplaceable = new ItemStack(Material.BOOK);
            ItemMeta unplaceableMeta = unplaceable.getItemMeta();
            unplaceableMeta.setDisplayName(ChatColor.YELLOW + "Unplaceable I");
            unplaceableMeta.setLore(Arrays.asList(" ", ChatColor.GRAY +  "This enchantment keeps any", ChatColor.GRAY + "item from getting placed!"));
            unplaceable.setItemMeta(unplaceableMeta);

            inv.setItem(11, unplaceable);

            // CLOSE BUTTON
            ItemStack close = new ItemStack(Material.BARRIER);
            ItemMeta closeMeta = close.getItemMeta();
            closeMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLOSE MENU");
            close.setItemMeta(closeMeta);

            inv.setItem(22, close);


            // FILL FRAMES
            ItemStack frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            for (int i : new int[]{0,1,2,3,4,5,6,7,8,9,12,13,14,15,16,17,18,19,20,21,23,24,25,26}) {
                inv.setItem(i, frame);
            }

            player.openInventory(inv);
        }

        return true;
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        perms.remove(e.getPlayer().getUniqueId());
    }

}
