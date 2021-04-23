package com.sigilmine.infinitychest.util;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.InfiniteItem;
import com.sigilmine.infinitychest.files.DataFile;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataUtil {

    private static final DataFile dataManager = InfinityChest.get().getDataFile();

    public static boolean hasBackpack(Player player) {
        return dataManager.getFileConfiguration().contains("infinitechests." + player.getUniqueId().toString());
    }

    public static InfiniteChest getInfiniteChest(Player player) {
        final FileConfiguration dataConfig = dataManager.getFileConfiguration();
        final String path = "infinitechests." + player.getUniqueId().toString() + ".";
        if (!hasBackpack(player)) {
            dataConfig.set(path + "items", new ArrayList<>());
            dataConfig.set(path + "autoCollect", false);
            dataManager.saveFileConfiguration();
            return new InfiniteChest(player);
        }
        List<InfiniteItem> items = dataConfig.getStringList(path + "items").stream()
                .map(item -> {
                    String[] parts = item.split(":");
                    if (parts.length != 2)
                        return null;
                    return new InfiniteItem(Material.matchMaterial(parts[0]), Long.parseLong(parts[1]));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        boolean autoCollect = dataConfig.getBoolean(path + "autoCollect", false);
        return new InfiniteChest(player, items, autoCollect);
    }

    public static void updateInfiniteChest(InfiniteChest infiniteChest) {
        final FileConfiguration dataConfig = dataManager.getFileConfiguration();
        String path = "infinitechests." + infiniteChest.getPlayer().getUniqueId().toString() + ".";
        dataConfig.set(path + "items", infiniteChest.steriliseItems());
        dataConfig.set(path + "autoCollect", infiniteChest.isAutoCollect());
        dataManager.saveFileConfiguration();
    }


}
