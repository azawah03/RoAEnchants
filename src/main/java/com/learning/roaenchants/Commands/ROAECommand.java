package com.learning.roaenchants.Commands;

import com.learning.roaenchants.Listeners.MenuListener;
import com.learning.roaenchants.RoAEnchants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

// THIS IS API FOR SOUT ANSI COLORS!
import org.bukkit.permissions.PermissionAttachment;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.util.*;
import java.util.logging.Level;

public class ROAECommand implements CommandExecutor, Listener {

    //TO-DO
    // - COMMAND TAB COMPLETE - COMPLETED
    // - PERMISSIONS - COMPLETED
    // - UNPLACEABLE - COMPLETED
    // - make sure console can give enchantments - COMPLETED

    private NamespacedKey unbreakableKey;
    private NamespacedKey unplaceableKey;
    private NamespacedKey doubleJumpKey;

    private HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    private RoAEnchants main;


    public ROAECommand(RoAEnchants main, NamespacedKey unbreakableKey, NamespacedKey unplaceableKey, NamespacedKey doubleJumpKey) {
        this.main = main;
        this.unbreakableKey = unbreakableKey;
        this.unplaceableKey = unplaceableKey;
        this.doubleJumpKey = doubleJumpKey;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        // List of enchants
        List<String> enchantments = Arrays.asList("unbreakable", "unplaceable", "doubleJump");
        boolean isValidEnchantment = false;
        String enchantToAdd = "unknown";

        //
        // CONSOLE COMMANDS
        //

        if (!(sender instanceof Player)) {
            AnsiConsole.systemInstall();

            if (args.length != 0) {
                for (String enchant : enchantments) {
                    isValidEnchantment = args[0].equalsIgnoreCase(enchant);
                    if (isValidEnchantment) {
                        enchantToAdd = args[0];
                        break;
                    }
                }

                if (args.length >= 2) {
                    if (isValidEnchantment) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if (target != null) {
                            // give target the enchant

                            ItemStack customEnchant = new ItemStack(Material.ENCHANTED_BOOK);
                            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) customEnchant.getItemMeta();

                            if (enchantToAdd.equalsIgnoreCase("unbreakable")) {

                                bookMeta.addStoredEnchant(Enchantment.getByKey(unbreakableKey), 1, false);
                                bookMeta.setDisplayName(ChatColor.DARK_PURPLE + "Unbreakable I");
                                bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from losing durability!"));

                            } else if (enchantToAdd.equalsIgnoreCase("unplaceable")) {

                                bookMeta.addStoredEnchant(Enchantment.getByKey(unplaceableKey), 1, false);
                                bookMeta.setDisplayName(ChatColor.YELLOW + "Unplaceable I");
                                bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from getting placed!"));

                            } else if (enchantToAdd.equalsIgnoreCase("doubleJump")) {
                                if (args.length == 3) {
                                    try {
                                        int enchantLevel = Integer.parseInt(args[2]);
                                        switch (enchantLevel) {
                                            case 1:
                                                bookMeta.addStoredEnchant(Enchantment.getByKey(doubleJumpKey), 1, false);
                                                bookMeta.setDisplayName(ChatColor.GREEN + "Double Jump I");
                                                bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "10 second cooldown", ChatColor.GRAY + "between every double jump.", "", ChatColor.RED + " " + ChatColor.BOLD + ChatColor.UNDERLINE + "BOOTS ENCHANTMENT"));

                                                break;
                                            case 2:
                                                bookMeta.addStoredEnchant(Enchantment.getByKey(doubleJumpKey), 2, false);
                                                bookMeta.setDisplayName(ChatColor.GREEN + "Double Jump II");
                                                bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "5 second cooldown", ChatColor.GRAY + "between every double jump.", "", ChatColor.RED + " " + ChatColor.BOLD + ChatColor.UNDERLINE + "BOOTS ENCHANTMENT"));
                                                break;
                                            case 3:
                                                bookMeta.addStoredEnchant(Enchantment.getByKey(doubleJumpKey), 3, false);
                                                bookMeta.setDisplayName(ChatColor.GREEN + "Double Jump III");
                                                bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "No cooldown between", ChatColor.GRAY + "every double jump.", "", ChatColor.RED + " " + ChatColor.BOLD + ChatColor.UNDERLINE + "BOOTS ENCHANTMENT"));
                                                break;
                                            default:
                                                throw new NumberFormatException("Invalid enchant level input: " + enchantLevel);
                                        }

                                    } catch (NumberFormatException e) {
                                        Ansi needLevel = ansi()
                                                .fg(RED)
                                                .a("THIS ENCHANT NEEDS A VALID LEVEL [1-3] SPECIFIED... ")
                                                .reset();

                                        System.out.println(needLevel);

                                        AnsiConsole.systemUninstall();
                                        return true;
                                    }

                                } else {
                                    Ansi needLevel = ansi()
                                            .fg(RED)
                                            .a("THIS ENCHANT NEEDS A LEVEL [1-3] SPECIFIED... ")
                                            .reset();

                                    System.out.println(needLevel);

                                    AnsiConsole.systemUninstall();
                                    return true;
                                }
                            }

                            customEnchant.setItemMeta(bookMeta);

                            target.getInventory().addItem(customEnchant);
                            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            target.sendMessage(ChatColor.GREEN + "You have been rewarded with the " + enchantToAdd.toUpperCase() + " enchantment!");

                            Ansi successMessage = ansi()
                                    .fg(GREEN)
                                    .a(target.getName().toUpperCase() + " HAS BEEN GIVEN THE " + enchantToAdd.toUpperCase() + " ENCHANTMENT!")
                                    .reset();

                            System.out.println(successMessage);

                            AnsiConsole.systemUninstall();
                            return true;

                        } else {
                            Ansi notOnline = ansi()
                                    .fg(RED)
                                    .a("PLAYER IS NOT ONLINE! TRY AGAIN...")
                                    .reset();

                            System.out.println(notOnline);

                            AnsiConsole.systemUninstall();
                            return true;
                        }
                    } else {
                        Ansi invalidEnchant = ansi()
                                .fg(RED)
                                .a("INVALID ENCHANTMENT! TRY AGAIN...")
                                .reset();

                        System.out.println(invalidEnchant);
                    }
                } else {
                    correctUsageConsole();
                }
            } else {
                correctUsageConsole();
            }

            AnsiConsole.systemUninstall();
            return true;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //
        // PLAYER COMMANDS
        //

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

        if (args.length != 0) {
            for (String enchant : enchantments) {
                isValidEnchantment = args[0].equalsIgnoreCase(enchant);
                if (isValidEnchantment) {
                    enchantToAdd = args[0];
                    break;
                }
            }
        } else {
            displayHelpMessage(player);
            return true;
        }


        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                displayHelpMessage(player);
                return true;
                //Checking if the enchant they put in is valid
            } else if (isValidEnchantment) {

                if (enchantToAdd.equalsIgnoreCase("doublejump") && args.length == 1) {
                    player.sendMessage(ChatColor.RED + "This enchant needs a level specified.\n Usage: /roae doublejump <level 1-3>");
                    return true;
                }

                // enchant players item
                if ((enchantToAdd.equalsIgnoreCase("unbreakable")
                        || enchantToAdd.equalsIgnoreCase("unplaceable"))
                        && args.length == 1
                        || args[1].matches("\\d+")) {
                    if (!player.getInventory().getItemInMainHand().getType().isAir()) {
                        ItemStack itemInHand = player.getInventory().getItemInMainHand();
                        if (enchantToAdd.equalsIgnoreCase("unbreakable")) {
                            //add unbreakable enchant to item in hand
                            ItemMeta itemMeta = itemInHand.getItemMeta();

                            List<String> lore = itemMeta.getLore();

                            if (lore == null) {
                                lore = new ArrayList<>();
                            }
                            lore.add(ChatColor.GRAY + "Unbreakable I");
                            itemMeta.setLore(lore);

                            itemInHand.setItemMeta(itemMeta);

                            itemInHand.addUnsafeEnchantment(Enchantment.getByKey(unbreakableKey), 1);
                            player.sendMessage(ChatColor.GREEN + "Successfully enchanted item with " + enchantToAdd.toUpperCase() + "!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            return true;
                        } else if (enchantToAdd.equalsIgnoreCase("unplaceable")) {
                            //add unplaceable enchant to item in hand
                            ItemMeta itemMeta = itemInHand.getItemMeta();

                            List<String> lore = itemMeta.getLore();

                            if (lore == null) {
                                lore = new ArrayList<>();
                            }
                            lore.add(ChatColor.GRAY + "Unplaceable I");
                            itemMeta.setLore(lore);

                            itemInHand.setItemMeta(itemMeta);

                            itemInHand.addUnsafeEnchantment(Enchantment.getByKey(unplaceableKey), 1);
                            player.sendMessage(ChatColor.GREEN + "Successfully enchanted item with " + enchantToAdd.toUpperCase() + "!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                            return true;
                        } else if (enchantToAdd.equalsIgnoreCase("doubleJump")) {
                            //add doublejump to item in hand
                            if (!(itemInHand.getType() == Material.LEATHER_BOOTS ||
                                    itemInHand.getType() == Material.IRON_BOOTS ||
                                    itemInHand.getType() == Material.CHAINMAIL_BOOTS ||
                                    itemInHand.getType() == Material.GOLDEN_BOOTS ||
                                    itemInHand.getType() == Material.DIAMOND_BOOTS ||
                                    itemInHand.getType() == Material.NETHERITE_BOOTS)) {

                                player.sendMessage(ChatColor.RED + "You must be holding boots in order to apply this enchantment!");
                                return true;
                            }

                            ItemMeta itemMeta = itemInHand.getItemMeta();

                            List<String> lore = itemMeta.getLore();

                            if (lore == null) {
                                lore = new ArrayList<>();
                            }

                            if (args.length == 2) {
                                try {
                                    int enchantLevel = Integer.parseInt(args[1]);
                                    int levelToAdd;
                                    switch (enchantLevel) {
                                        case 1:
                                            lore.add(ChatColor.GRAY + "Double Jump I");
                                            levelToAdd = 1;
                                            break;
                                        case 2:
                                            lore.add(ChatColor.GRAY + "Double Jump II");
                                            levelToAdd = 2;
                                            break;
                                        case 3:
                                            lore.add(ChatColor.GRAY + "Double Jump III");
                                            levelToAdd = 3;
                                            break;
                                        default:
                                            throw new NumberFormatException("Invalid enchant level input: " + enchantLevel);
                                    }

                                    itemMeta.setLore(lore);
                                    itemInHand.setItemMeta(itemMeta);

                                    itemInHand.addUnsafeEnchantment(Enchantment.getByKey(doubleJumpKey), levelToAdd);

                                    player.sendMessage(ChatColor.GREEN + "Successfully enchanted item with " + enchantToAdd.toUpperCase() + " " + enchantLevel + "!");
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                    return true;
                                } catch (NumberFormatException e) {
                                    player.sendMessage(ChatColor.RED + "This enchant needs a valid level specified.\n Usage: /roae doublejump <1-3>");
                                    return true;
                                }

                            } else {
                                player.sendMessage(ChatColor.RED + "This enchant needs a level specified.\n Usage: /roae doublejump <level 1-3>");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Please hold an item in order to enchant.");
                        return true;
                    }
                } else {
                    //Making sure there is an argument for the player
                    if (Bukkit.getPlayerExact(args[1]) != null) {
                        Player target = Bukkit.getPlayerExact(args[1]);

                        // give target the enchant

                        ItemStack customEnchant = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) customEnchant.getItemMeta();

                        //Check which key to use
                        if (enchantToAdd.equalsIgnoreCase("unbreakable")) {
                            bookMeta.addStoredEnchant(Enchantment.getByKey(unbreakableKey), 1, false);
                            bookMeta.setDisplayName(ChatColor.DARK_PURPLE + "Unbreakable I");
                            bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from losing durability!"));
                        } else {
                            bookMeta.addStoredEnchant(Enchantment.getByKey(unplaceableKey), 1, false);
                            bookMeta.setDisplayName(ChatColor.YELLOW + "Unplaceable I");
                            bookMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This enchantment keeps any", ChatColor.GRAY + "item from getting placed!"));
                        }

                        customEnchant.setItemMeta(bookMeta);

                        target.getInventory().addItem(customEnchant);
                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        target.sendMessage(ChatColor.GREEN + "You have been rewarded with the " + enchantToAdd.toUpperCase() + " enchantment!");
                        player.sendMessage(ChatColor.GREEN + "You have given the " + enchantToAdd.toUpperCase() + " enchantment to " + target.getPlayer().getName() + ".");
                        return true;

                    } else {
                        player.sendMessage(ChatColor.RED + "This player is not online!");
                        return true;
                    }
                }

            } else {
                displayHelpMessage(player);
                player.sendMessage(ChatColor.RED + "Incorrect Usage or Invalid Enchantment!");
                return true;
            }
        }

        return true;

    }

    private static void correctUsageConsole() {
        Ansi correctUsage = ansi()
                .fg(RED)
                .a("Correct Usage: /roae <enchant> <player>")
                .reset();

        System.out.println(correctUsage);
    }

    private static void displayHelpMessage(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        List<String> helpMessage = Arrays.asList("", ChatColor.GREEN +  "" + ChatColor.UNDERLINE + "RoA Enchants Help Menu", "",
                ChatColor.GREEN + "- " + ChatColor.YELLOW + "/roae <enchantment> <user> - Give User an Enchantment Book.",
                ChatColor.GREEN + "- " + ChatColor.YELLOW + "/roae <enchantment> - Enchant Item In Hand.",
                ChatColor.GREEN + "- " + ChatColor.YELLOW + "/roae help - Display Help Menu.",
                ChatColor.GREEN + "- " + ChatColor.YELLOW + "/roaes - GUI of the enchantments.", "",
                ChatColor.RED + "" + ChatColor.UNDERLINE + "ADMIN ONLY COMMANDS!", "");


        for (String line : helpMessage) {
            player.sendMessage(line);
        }
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        perms.remove(e.getPlayer().getUniqueId());
    }
}
