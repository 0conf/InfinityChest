package com.sigilmine.infinitychest.Commands;

import com.sigilmine.infinitychest.Entities.InfiniteMenu;
import com.sigilmine.infinitychest.Entities.SubCommand;
import com.sigilmine.infinitychest.Util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class SubView implements SubCommand {

    @Override
    public boolean consoleUse() {
        return false;
    }

    @Override
    public String getName() {
        return "view";
    }

    @Override
    public String getUsage() {
        return "(player)";
    }

    @Override
    public String getPermission() {
        return "infinitychest.view";
    }

    @Override
    public String getDescription() {
        return "View another player's infinity chest";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        final String path = "commands.view.";
        if (args.length != 1) {
            player.sendMessage(MessageUtil.getMessage(path + "insufficient-arguments"));
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-player"));
            return;
        }
        player.openInventory(new InfiniteMenu(target, false).getInventory());

        player.sendMessage(MessageUtil.getMessage(path + "success",
                "%player%", target.getName()));
    }

}
