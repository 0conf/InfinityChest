package com.sigilmine.infinitychest.Entities;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.Util.ChestUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class InfiniteChest {

    private Player player;
    private List<InfiniteItem> items = new ArrayList<>();
    private boolean autoCollect;

    public InfiniteChest(Player player) {
        this.player = player;
    }

    public InfiniteChest(final Player player, List<InfiniteItem> items, boolean autoCollect) {
        this.player = player;
        this.items.addAll(items);
        this.autoCollect = autoCollect;
    }

    public boolean addItem(Material material, long amount) {
        InfiniteItem infiniteItem = ChestUtil.getInfiniteItem(this, material);
        if (infiniteItem != null) {
            infiniteItem.setAmount(infiniteItem.getAmount() + amount);
            return true;
        }
        if (getItems().size() >= ChestUtil.getMaxSlots(player))
            return false;
        getItems().add(new InfiniteItem(material, amount));
        return true;
    }

    public List<String> steriliseItems() {
        return items.stream()
                .map(item -> item.getMaterial().name() + ":" + item.getAmount())
                .collect(Collectors.toList());
    }

    public boolean isAutoCollect() {
        return this.autoCollect;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setAutoCollect(boolean set) {
        autoCollect = set;
    }

    public List<InfiniteItem> getItems() {
        return this.items;
    }

    public void setItems(List<InfiniteItem> items) {
        this.items = items;
    }
}
