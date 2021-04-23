package com.sigilmine.infinitychest.events;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.InfiniteItem;
import com.sigilmine.infinitychest.tasks.PlayerLoadTask;
import com.sigilmine.infinitychest.tasks.PlayerUnloadTask;
import com.sigilmine.infinitychest.util.ChestUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEvents implements Listener {

    private static InfinityChest infinityChest = InfinityChest.get();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new PlayerLoadTask(event.getPlayer()).runTask(infinityChest);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        new PlayerUnloadTask(event.getPlayer()).runTask(infinityChest);
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return;
        final Player player = (Player) event.getEntity();
        final InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);
        if (!infiniteChest.isAutoCollect())
            return;
        final ItemStack itemStack = event.getItem().getItemStack();
        if (ChestUtil.isBannedItem(itemStack))
            return;
        InfiniteItem infiniteItem = ChestUtil.getInfiniteItem(infiniteChest, itemStack.getType());
        if (infiniteItem == null)
            return;
        if (infiniteChest.addItem(itemStack.getType(), itemStack.getAmount())) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }
}
