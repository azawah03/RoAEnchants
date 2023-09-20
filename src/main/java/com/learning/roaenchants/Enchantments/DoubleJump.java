package com.learning.roaenchants.Enchantments;

import com.learning.roaenchants.RoAEnchants;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJump extends Enchantment implements Listener {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, Boolean> isFunctionBeingUsed = new HashMap<>();
    private static final Map<UUID, BukkitTask> cooldownTasks = new HashMap<>();

    public DoubleJump() {
        super(NamespacedKey.minecraft("double_jump"));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (player.getGameMode() == GameMode.SURVIVAL) {
            ItemStack boots = player.getInventory().getBoots();
            if (boots != null && boots.containsEnchantment(this)) {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();

        // Check if the player is allowed to fly (prevents initial flying when equipping boots)
        if (!isAllowedToFly(player)) {
            e.setCancelled(true);
            return;
        }

        int cooldown = getRemainingCooldown(player, "doubleJumpCooldown");
        if (cooldown != 0) {
            e.setCancelled(true);
            sendCooldownMessage(player, cooldown);
            return;
        }

        if (!isFunctionBeingUsed(player)) {
            setFunctionBeingUsed(player, true);

            if (player.getGameMode() == GameMode.SURVIVAL) {
                ItemStack boots = player.getInventory().getBoots();
                if (boots != null && boots.containsEnchantment(this)) {
                    int enchantLevel = boots.getEnchantmentLevel(this);

                    int cooldownDuration;
                    switch (enchantLevel) {
                        case 1:
                            cooldownDuration = 10;
                            break;
                        case 2:
                            cooldownDuration = 7;
                            break;
                        case 3:
                            cooldownDuration = 3;
                            break;
                        default:
                            return; // Invalid enchantment level
                    }

                    if (!hasCooldown(player, "doubleJumpCooldown")) {
                        if (e.isFlying() && player.getLocation().subtract(0, 2, 0).getBlock().getType() != Material.AIR) {
                            player.setVelocity(player.getLocation().getDirection().multiply(1).setY(1));
                            applyCooldown(player, "doubleJumpCooldown", cooldownDuration);
                            e.setCancelled(true);

                            if (cooldownDuration > 0) {
                                sendCooldownMessage(player, cooldownDuration);

                                // Start a BukkitRunnable to remove the cooldown after its duration
                                BukkitTask task = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        cooldowns.remove(player.getUniqueId());
                                        cooldownTasks.remove(player.getUniqueId());
                                    }
                                }.runTaskLater(JavaPlugin.getPlugin(RoAEnchants.class), cooldownDuration * 20); // Convert seconds to ticks

                                // Store the task so we can cancel it if needed
                                cooldownTasks.put(player.getUniqueId(), task);
                            }
                        }
                    } else {
                        int remainingCooldown = getRemainingCooldown(player, "doubleJumpCooldown");
                        if (remainingCooldown <= 0) {
                            cooldowns.remove(player.getUniqueId());
                            BukkitTask task = cooldownTasks.get(player.getUniqueId());
                            if (task != null) {
                                task.cancel();
                                cooldownTasks.remove(player.getUniqueId());
                            }
                        } else {
                            e.setCancelled(true);
                            sendCooldownMessage(player, remainingCooldown);
                        }
                    }
                }
            }
            setFunctionBeingUsed(player, false);
        }
    }

    private boolean isFunctionBeingUsed(Player player) {
        return isFunctionBeingUsed.getOrDefault(player.getUniqueId(), false);
    }

    private void setFunctionBeingUsed(Player player, boolean value) {
        isFunctionBeingUsed.put(player.getUniqueId(), value);
    }

    private boolean isAllowedToFly(Player player) {
        return player.getAllowFlight();
    }

    private void sendCooldownMessage(Player player, int remainingCooldown) {
        String message = ChatColor.WHITE + "Please wait " + ChatColor.RED + ChatColor.UNDERLINE + remainingCooldown + ChatColor.WHITE + " seconds before double jumping!";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    private boolean hasCooldown(Player player, String key) {
        return cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()) >= System.currentTimeMillis() / 1000;
    }

    private int getRemainingCooldown(Player player, String key) {
        if (hasCooldown(player, key)) {
            long currentTime = System.currentTimeMillis() / 1000;
            long cooldownTime = cooldowns.get(player.getUniqueId());
            int remainingCooldown = (int) (cooldownTime - currentTime);
            return Math.max(0, remainingCooldown);
        }
        return 0;
    }

    private void applyCooldown(Player player, String key, int seconds) {
        long cooldownTime = System.currentTimeMillis() / 1000 + seconds;
        cooldowns.put(player.getUniqueId(), cooldownTime);
    }

    public boolean isBootWithEnchantment(ItemStack item) {
        return isBoot(item) && item.getEnchantments().containsKey(this);
    }

    public boolean isBoot(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_BOOTS ||
                type == Material.IRON_BOOTS ||
                type == Material.GOLDEN_BOOTS ||
                type == Material.DIAMOND_BOOTS ||
                type == Material.CHAINMAIL_BOOTS ||
                type == Material.NETHERITE_BOOTS;
    }

    public String getName() {
        return "Double Jump";
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getStartLevel() {
        return 1;
    }

    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_FEET;
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
