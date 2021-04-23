package com.sigilmine.infinitychest.tasks;

import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerLoadTask extends BukkitRunnable {

    private final Player player;

    public PlayerLoadTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        ChestUtil.addInfiniteChest(DataUtil.getInfiniteChest(player));
    }
}
