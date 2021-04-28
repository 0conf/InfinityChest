package com.sigilmine.infinitychest.Tasks;

import com.sigilmine.infinitychest.Entities.InfiniteChest;
import com.sigilmine.infinitychest.Util.ChestUtil;
import com.sigilmine.infinitychest.Util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class PlayerUnloadTask extends BukkitRunnable {

    private final Player player;

    public PlayerUnloadTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        //long load = System.currentTimeMillis();
        InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);
        if (infiniteChest == null)
            return;
        DataUtil.updateInfiniteChest(infiniteChest);
        ChestUtil.removeInfiniteChest(infiniteChest);
        //long fina =  System.currentTimeMillis() - load;
        //System.out.println("[ZitoriaProfiler] SaveTask Finished (InfinityChest): " + fina + "ms");
    }
}
