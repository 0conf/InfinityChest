package com.sigilmine.infinitychest.commands;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.InfiniteItem;
import com.sigilmine.infinitychest.entities.SubCommand;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.LimitUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SubSell implements SubCommand {

    private static Economy economy = InfinityChest.getEconomy();

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getUsage() {
        return "(item) (amount)";
    }

    @Override
    public String getPermission() {
        return "infinitychest.sell";
    }

    @Override
    public String getDescription() {
        return "Sell items from your Infinity Chest";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        final String path = "commands.sell.";
        if (args.length < 2) {
            player.sendMessage(MessageUtil.getMessage(path + "insufficient-arguments"));
            return;
        }
        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            player.sendMessage(MessageUtil.getMessage(path + "unknown-item"));
            return;
        }
        long quantity;
        InfiniteChest chest = ChestUtil.getInfiniteChest(player);
        InfiniteItem item = ChestUtil.getInfiniteItem(chest, material);
        if (item == null) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-item"));
            return;
        }
        if (args[1].equalsIgnoreCase("All")) {
            quantity = item.getAmount();
        } else {
            try {
                quantity = Long.parseLong(args[1]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(MessageUtil.getMessage(path + "invalid-quantity"));
                return;
            }
        }
        if (quantity <= 0) {
            player.sendMessage(MessageUtil.getMessage(path + "invalid-quantity"));
            return;
        }
        if (quantity > item.getAmount()) {
            //player.sendMessage(MessageUtil.getMessage(path + "insufficient-items"));
            player.sendMessage(MessageUtil.getMessage(path + "insufficient-items", "%amount%", quantity));
            return;
        }
        if (item.getSingleValue() <= 0) {
            player.sendMessage(MessageUtil.getMessage(path + "illegal-item"));
            return;
        }
        long limit = LimitUtil.getLimit(player);
        long maxSell = LimitUtil.getMaxSellAmount(player);
        if (limit >= maxSell) {
            player.sendMessage(MessageUtil.getMessage(path + "limit-exceeded"));
            return;
        }
        quantity = Math.min(quantity, maxSell - limit);
        double totalSellPrice = item.getSingleValue() * quantity;
        final EconomyResponse economyResponse = economy.depositPlayer(player, totalSellPrice);
        if (!economyResponse.transactionSuccess()) {
            player.sendMessage(MessageUtil.getMessage(path + "error"));
            return;
        }
        item.setAmount(item.getAmount() - quantity);
        LimitUtil.setLimit(player, limit + quantity);
        player.sendMessage(MessageUtil.getMessage(path + "success",
                "%amount%", MessageUtil.longToString(quantity),
                "%material%", MessageUtil.getFriendlyMaterialName(material),
                "%price%", MessageUtil.formatDouble(economyResponse.amount),
                "%remaining%", MessageUtil.longToString(item.getAmount())));
        if (item.getAmount() <= 0)
            chest.getItems().remove(item);
    }
}