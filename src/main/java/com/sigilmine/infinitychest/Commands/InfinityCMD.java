package com.sigilmine.infinitychest.Commands;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.Entities.Command;
import com.sigilmine.infinitychest.Entities.InfiniteMenu;
import com.sigilmine.infinitychest.Entities.SubCommand;
import com.sigilmine.infinitychest.Util.ChestUtil;
import com.sigilmine.infinitychest.Util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class InfinityCMD extends Command {

    public InfinityCMD(InfinityChest main) {
        super(main, "infinitychest");
        this
                .registerSubCommand(new SubAddSlots())
                .registerSubCommand(new SubGive())
                .registerSubCommand(new SubSell())
                .registerSubCommand(new SubAutoCollect())
                .registerSubCommand(new SubEmpty())
                .registerSubCommand(new SubView())
        ;
    }

    @Override
    public void onUse(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.getMessage("commands.no-console"));
            return;
        }
        final Player player = (Player) sender;
        if (!player.hasPermission("infinitychest.access")) {
            player.sendMessage(MessageUtil.getMessage("commands.no-permission"));
            return;
        }
        if (ChestUtil.getMaxSlots(player) <= 0) {
            player.sendMessage(MessageUtil.getMessage("commands.no-slots"));
            return;
        }
        player.openInventory(new InfiniteMenu(player, true).getInventory());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return this.getSubCommands().stream()
                    .map(SubCommand::getName)
                    .filter(subCommand -> subCommand.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("empty") || args[0].equalsIgnoreCase("view")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
