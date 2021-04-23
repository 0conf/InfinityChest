package com.sigilmine.infinitychest.commands;

import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.SubCommand;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import org.bukkit.entity.Player;

public class SubAutoCollect implements SubCommand {

    @Override
    public String getName() {
        return "autocollect";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getPermission() {
        return "infinitychest.autocollect";
    }

    @Override
    public String getDescription() {
        return "Toggle whether items picked up should automatically go to your chest or not.";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        final InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);
        infiniteChest.setAutoCollect(!infiniteChest.isAutoCollect());
        if(infiniteChest.isAutoCollect())
            player.sendMessage(MessageUtil.getMessage("commands.autocollect.enabled"));
        else
            player.sendMessage(MessageUtil.getMessage("commands.autocollect.disabled"));
    }
}
