package com.sigilmine.infinitychest.entities;

import com.sigilmine.infinitychest.util.MessageUtil;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {

    default boolean consoleUse() {
        return false;
    }

    String getName();

    String getUsage();

    String getPermission();

    String getDescription();

    String[] getAliases();

    void onCommandByPlayer(Player player, String[] args);

    default void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        if (!consoleUse()) sender.sendMessage(MessageUtil.getMessage("commands.no-console"));
    }
}