package com.learning.roaenchants.TabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ROAETab implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("unbreakable","unplaceable","doublejump"), new ArrayList<>());
        } else if (args.length == 2) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return StringUtil.copyPartialMatches(args[1], names, new ArrayList<>());
        } else if (args.length == 3) {
            return StringUtil.copyPartialMatches(args[2], Arrays.asList("1","2","3"), new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
