package com.sigilmine.infinitychest.commands;

import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.SubCommand;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SubEmpty implements SubCommand {

    @Override
    public boolean consoleUse() {
        return true;
    }

    @Override
    public String getName() {
        return "empty";
    }

    @Override
    public String getUsage() {
        return "(player)";
    }

    @Override
    public String getPermission() {
        return "infinitychest.empty";
    }

    @Override
    public String getDescription() {
        return "Empty a player's infinity chest";
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
        final String path = "commands.empty.";
        if (args.length == 0) {
            sender.sendMessage(MessageUtil.getMessage(path + "insufficient-arguments"));
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(MessageUtil.getMessage(path + "invalid-player"));
            return;
        }
        InfiniteChest chest = ChestUtil.getInfiniteChest(target);
        chest.setItems(new ArrayList<>());
        sender.sendMessage(MessageUtil.getMessage(path + "success",
                "%player%", target.getName()));
    }
}
