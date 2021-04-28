package com.sigilmine.infinitychest.Events;

import com.sigilmine.infinitychest.Entities.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class MenuEvents implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            event.setCancelled(true);
            ((Menu) holder).onClick(event);
        }
    }
}
