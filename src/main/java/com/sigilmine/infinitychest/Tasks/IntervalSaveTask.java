package com.sigilmine.infinitychest.Tasks;

import com.sigilmine.infinitychest.Util.ChestUtil;
import com.sigilmine.infinitychest.Util.DataUtil;
import com.sigilmine.infinitychest.Util.LoggerUtil;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class IntervalSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        ChestUtil.getInfiniteChests().forEach(DataUtil::updateInfiniteChest);
    }
}
