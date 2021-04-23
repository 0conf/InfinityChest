package com.sigilmine.infinitychest.tasks;

import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.DataUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class IntervalSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        ChestUtil.getInfiniteChests().forEach(DataUtil::updateInfiniteChest);
    }
}
