package com.sigilmine.infinitychest.commands;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.entities.SubCommand;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SubAddSlots implements SubCommand {


    @Override
    public boolean consoleUse() {
        return true;
    }

    @Override
    public String getName() {
        return "addslots";
    }

    @Override
    public String getUsage() {
        return "(player) (amount)";
    }

    @Override
    public String getPermission() {
        return "infinitychest.addslots";
    }

    @Override
    public String getDescription() {
        return "Increase the amount of slots a player can have";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        onCommand(player, args);
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        onCommand(sender, args);
    }

    private void onCommand(CommandSender sender, String[] args) {
        final LuckPerms luckPerms = InfinityChest.getLuckPerms();
        if (luckPerms == null) {
            sender.sendMessage(MessageUtil.format("&c[!] LuckPerms is disabled! Enable it to use this."));
            return;
        }
        final String path = "commands.addslots.";
        if (args.length != 2) {
            sender.sendMessage(MessageUtil.getMessage(path + "insufficient-arguments"));
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageUtil.getMessage(path + "invalid-player"));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(MessageUtil.getMessage(path + "invalid-amount"));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(MessageUtil.getMessage(path + "invalid-amount"));
            return;
        }
        int currentSlots = ChestUtil.getMaxSlots(target);
        int newSlots = currentSlots + amount;
        final User user = luckPerms.getUserManager().getUser(target.getUniqueId());
        if (user == null) {
            sender.sendMessage(MessageUtil.getMessage(path + "error"));
            return;
        }
        user.data().remove(PermissionNode.builder("infinitychest.slots." + currentSlots).value(true).build());
        user.data().add(PermissionNode.builder("infinitychest.slots." + newSlots).value(true).build());
        luckPerms.getUserManager().saveUser(user);
        sender.sendMessage(MessageUtil.getMessage(path + "success",
                "%amount%", Integer.toString(amount),
                "%player%", target.getName(),
                "%total%", Integer.toString(newSlots)));
    }
}
