package com.sigilmine.infinitychest.entities;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command implements CommandExecutor, TabCompleter {

    private List<SubCommand> subCommands = new ArrayList<>();

    private String name;

    public Command(InfinityChest main, String name) {
        this.name = name;
        main.getCommand(name).setExecutor(this);
        main.getCommand(name).setTabCompleter(this);
    }

    public Command registerSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            this.onUse(sender);
            return true;
        }

        SubCommand subCommand = subCommands.stream().filter(sub -> {
            if (sub.getName().equalsIgnoreCase(args[0])) {
                return true;
            }
            return Arrays.asList(sub.getAliases()).contains(args[0].toLowerCase());
        }).findFirst().orElse(null);

        if (subCommand == null) {
            sender.sendMessage(MessageUtil.getMessage("commands.invalid-subargument",
                    "%argument%", args[0]));
            return true;
        }

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(MessageUtil.getMessage("commands.no-permission"));
            return true;
        }
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        if (sender instanceof Player) {
            subCommand.onCommandByPlayer((Player) sender, newArgs);
            return true;
        }
        subCommand.onCommandByConsole((ConsoleCommandSender) sender, newArgs);
        return true;
    }

    public void onUse(CommandSender sender) {
        sender.sendMessage(MessageUtil.format("&7&l--- &r&2" + StringUtils.capitalize(name) + " &bSubCommands &7&l---"));
        subCommands.forEach(sub -> {
            sender.sendMessage(MessageUtil.format("&2/" + name + " " + sub.getName() + (sub.getUsage() == null || sub.getUsage().length() == 0 ? "" : " &b" + sub.getUsage()) + " &e&l- &7" + sub.getDescription()));
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return subCommands.stream().filter(sub -> {
                if (sub.getPermission() == null) return true;
                return sender.hasPermission(sub.getPermission());
            })
                    .map(SubCommand::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<SubCommand> getSubCommands() {
        return Collections.unmodifiableList(subCommands);
    }
}
