package com.sigilmine.infinitychest.commands;

import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.InfiniteItem;
import com.sigilmine.infinitychest.entities.SubCommand;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SubGive implements SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getUsage() {
        return "(player) (item) (amount)";
    }

    @Override
    public String getPermission() {
        return "infinitychest.give";
    }

    @Override
    public String getDescription() {
        return "Give another player items from your infinity chest.";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        final String path = "commands.give.";
        if (args.length != 3) {
            player.sendMessage(MessageUtil.getMessage(path + "insufficient-arguments"));
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-player"));
            return;
        }
        Material material = Material.matchMaterial(args[1]);
        if (material == null) {
            player.sendMessage(MessageUtil.getMessage(path + "unknown-item"));
            return;
        }
        long quantity;
        try {
            quantity = Long.parseLong(args[2]);
        } catch (NumberFormatException nfe) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-quantity"));
            return;
        }
        InfiniteChest chest = ChestUtil.getInfiniteChest(player);
        InfiniteItem item = ChestUtil.getInfiniteItem(chest, material);
        if (quantity <= 0) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-quantity"));
            return;
        }
        if (item == null) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-item"));
            return;
        }
        if (quantity > item.getAmount()) {
            player.sendMessage(MessageUtil.getMessage(path + "insufficient-items"));
            return;
        }
        InfiniteChest targetChest = ChestUtil.getInfiniteChest(target);
        if (targetChest.addItem(material, quantity)) {
            item.setAmount(item.getAmount() - quantity);
            player.sendMessage(MessageUtil.getMessage(path + "success",
                    "%player%", target.getName(),
                    "%amount%", Long.toString(quantity),
                    "%material%", material.toString(),
                    "%remaining%", MessageUtil.longToString(item.getAmount())));
            target.sendMessage(MessageUtil.getMessage(path + "item-received",
                    "%player%", player.getName(),
                    "%amount%", Long.toString(quantity),
                    "%material%", material.toString()));
            if (item.getAmount() <= 0)
                chest.getItems().remove(item);
            return;
        }
        player.sendMessage(MessageUtil.getMessage(path + "target-full", "%player%", target.getName()));
    }
}
