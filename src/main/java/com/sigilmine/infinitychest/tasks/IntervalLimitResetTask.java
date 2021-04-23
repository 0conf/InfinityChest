package com.sigilmine.infinitychest.tasks;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.util.LimitUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class IntervalLimitResetTask extends BukkitRunnable {

    @Override
    public void run() {
        InfinityChest.get().getCs().sendMessage(ChatColor.AQUA + "[InfinityChest] Successfully reset limits!");
        LimitUtil.resetLimits();
    }

}
