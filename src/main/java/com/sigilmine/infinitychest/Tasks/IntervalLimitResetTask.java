package com.sigilmine.infinitychest.Tasks;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.Util.LimitUtil;
import com.sigilmine.infinitychest.Util.LoggerUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class IntervalLimitResetTask extends BukkitRunnable {

    @Override
    public void run() {
        LoggerUtil.log(LoggerUtil.LogLevel.GENERIC, "Successfully reset limits!");
        LimitUtil.resetLimits();
    }

}
