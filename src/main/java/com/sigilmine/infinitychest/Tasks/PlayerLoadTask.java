package com.sigilmine.infinitychest.Tasks;

import com.sigilmine.infinitychest.Util.ChestUtil;
import com.sigilmine.infinitychest.Util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class PlayerLoadTask extends BukkitRunnable {

    private final Player player;

    public PlayerLoadTask(Player player) {
        this.player = player;
    }


    @Override
    public void run() {
        //long load = System.currentTimeMillis();
        //System.out.println("[ZitoriaProfiler] LoadTask Initiated (InfinityChest): " + load);
        ChestUtil.addInfiniteChest(DataUtil.getInfiniteChest(player));
        //long fina =  System.currentTimeMillis() - load;
        //System.out.println("[ZitoriaProfiler] LoadTask Finished (InfinityChest): " + fina + "ms");
    }
}
