package com.sigilmine.infinitychest.tasks;

import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.DataUtil;
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
        InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);
        if (infiniteChest == null)
            return;
        DataUtil.updateInfiniteChest(infiniteChest);
        ChestUtil.removeInfiniteChest(infiniteChest);
    }
}
