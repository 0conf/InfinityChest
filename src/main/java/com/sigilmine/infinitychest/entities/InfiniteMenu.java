package com.sigilmine.infinitychest.entities;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.sigilmine.infinitychest.util.MessageUtil.format;

public class InfiniteMenu implements Menu {

    final private InfinityChest plugin = InfinityChest.get();
    final private FileConfiguration config;
    final private String path = "main-menu.";
    final Player player;
    final boolean editable;
    final private Inventory inventory;

    public InfiniteMenu(Player player, boolean editable) {
        this.config = plugin.getConfig();
        this.player = player;
        this.editable = editable;
        String name = format(config.getString("main-menu.title"));
        int size = config.getInt("main-menu.size", 27);
        this.inventory = Bukkit.createInventory(this, size, name);

        final Material lockedMaterial = Material.matchMaterial(config.getString(path + "locked-item.material", "DIRT"));
        final ItemStack lockedItem = new ItemStack(lockedMaterial != null ? lockedMaterial : Material.DIRT);
        final ItemMeta lockedMeta = lockedItem.getItemMeta();
        final String lockedName = config.getString(path + "locked-item.name", "");
        if (!lockedName.isEmpty()) {
            lockedMeta.setDisplayName(MessageUtil.format(lockedName));
        }
        lockedMeta.setLore(MessageUtil.format(config.getStringList(path + "locked-item.lore")));
        lockedItem.setItemMeta(lockedMeta);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, lockedItem);
        }
        for (int i = 0; i < ChestUtil.getMaxSlots(player); i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }

        final InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);

        int index = 0;
        final String itemName = config.getString(path + "infinite-item.name");
        final List<String> itemLore = config.getStringList(path + "infinite-item.lore");
        for (InfiniteItem infiniteItem : infiniteChest.getItems()) {
            Material material = infiniteItem.getMaterial();
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(format(itemName,
                    "%name%", MessageUtil.getFriendlyMaterialName(material)));
            itemMeta.setLore(format(itemLore,
                    "%quantity%", MessageUtil.longToString(infiniteItem.getAmount()),
                    "%single_value%", MessageUtil.formatDouble(infiniteItem.getSingleValue()),
                    "%total_value%", MessageUtil.formatDouble(infiniteItem.getTotalValue())));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(index, itemStack);
            index++;
        }
    }


    public void onClick(final InventoryClickEvent event) {
        if (!editable)
            return;
        final Player viewingPlayer = (Player) event.getWhoClicked();
        final int slot = event.getSlot();
        if (slot < 0)
            return;
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;
        boolean shiftClick = event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
        final InfiniteChest infiniteChest = ChestUtil.getInfiniteChest(player);
        if (event.getView().getTopInventory().equals(event.getClickedInventory())) {
            //WITHDRAW ITEMS
            if (slot >= infiniteChest.getItems().size())
                return;
            if (viewingPlayer.getInventory().firstEmpty() == -1) {
                viewingPlayer.closeInventory();
                viewingPlayer.sendMessage(MessageUtil.getMessage("chest.inventory-full"));
                return;
            }
            final InfiniteItem infiniteItem = infiniteChest.getItems().get(slot);
            final Material material = infiniteItem.getMaterial();
            final int maxSize = material.getMaxStackSize();
            int withdraw;
            if (shiftClick)
                withdraw = Arrays.stream(viewingPlayer.getInventory().getStorageContents())
                        .mapToInt(itemStack -> {
                            if (itemStack == null || itemStack.getType() == Material.AIR)
                                return maxSize;
                            if (itemStack.getType() == material)
                                return maxSize - itemStack.getAmount();
                            return 0;
                        }).sum();
            else if (event.getClick() == ClickType.LEFT)
                withdraw = maxSize;
            else
                withdraw = maxSize > 1 ? maxSize / 2 : 1;
            withdraw = Math.min((int) infiniteItem.getAmount(), withdraw);
            infiniteItem.setAmount(infiniteItem.getAmount() - withdraw);
            if (maxSize == 64)
                viewingPlayer.getInventory().addItem(new ItemStack(material, withdraw));
            else {
                int stacks = withdraw / maxSize;
                int remainder = withdraw % maxSize;
                for (int i = 0; i < stacks; i++)
                    viewingPlayer.getInventory().addItem(new ItemStack(material, maxSize));
                if (remainder > 0)
                    viewingPlayer.getInventory().addItem(new ItemStack(material, remainder));
            }

            viewingPlayer.sendMessage(MessageUtil.getMessage("chest.item-withdrawn",
                    "%amount%", Integer.toString(withdraw),
                    "%material%", MessageUtil.getFriendlyMaterialName(material),
                    "%remaining%", infiniteItem.getAmount()));
            if (infiniteItem.getAmount() <= 0)
                infiniteChest.getItems().remove(infiniteItem);
            viewingPlayer.openInventory(new InfiniteMenu(player, true).getInventory());
            return;
        }
        //DEPOSIT ITEMS
        if (ChestUtil.isBannedItem(clickedItem)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                viewingPlayer.closeInventory();
                viewingPlayer.sendMessage(MessageUtil.getMessage("chest.illegal-item"));
            }, 5L);
            return;
        }
        if (shiftClick) {
            Material clickedType = clickedItem.getType();
            AtomicBoolean fullChest = new AtomicBoolean(false);
            Arrays.stream(viewingPlayer.getInventory().getStorageContents())
                    .filter(Objects::nonNull)
                    .filter(itemStack -> itemStack.getType() == clickedType)
                    .filter(itemStack -> !ChestUtil.isBannedItem(itemStack))
                    .forEach(itemStack -> {
                        if (fullChest.get())
                            return;
                        if (!infiniteChest.addItem(itemStack.getType(), itemStack.getAmount())) {
                            fullChest.set(true);
                            viewingPlayer.closeInventory();
                            viewingPlayer.sendMessage(MessageUtil.getMessage("chest.chest-full"));
                            return;
                        }
                        itemStack.setAmount(0);
                    });
            if (!fullChest.get())
                viewingPlayer.openInventory(new InfiniteMenu(player, true).getInventory());
            return;
        }
        final int amount = clickedItem.getAmount();
        final int deposit;
        if (event.getClick() == ClickType.LEFT)
            deposit = amount;
        else
            deposit = amount > 1 ? amount / 2 : 1;
        if (infiniteChest.addItem(clickedItem.getType(), deposit)) {
            clickedItem.setAmount(amount - deposit);
            viewingPlayer.openInventory(new InfiniteMenu(player, true).getInventory());
            return;
        }
        viewingPlayer.closeInventory();
        viewingPlayer.sendMessage(MessageUtil.getMessage("chest.chest-full"));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
