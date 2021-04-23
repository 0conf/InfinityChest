package com.sigilmine.infinitychest.util;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.entities.InfiniteChest;
import com.sigilmine.infinitychest.entities.InfiniteItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class ChestUtil {

    private static List<InfiniteChest> infiniteChests = new ArrayList<>();

    public static void addInfiniteChest(InfiniteChest infiniteChest) {
        infiniteChests.add(infiniteChest);
    }

    public static void removeInfiniteChest(InfiniteChest infiniteChest) {
        infiniteChests.remove(infiniteChest);
    }

    public static InfiniteChest getInfiniteChest(Player player) {
        return infiniteChests.stream()
                .filter(infiniteChest -> infiniteChest.getPlayer().equals(player))
                .findFirst().orElse(null);
    }

    public static InfiniteItem getInfiniteItem(InfiniteChest chest, Material material) {
        return chest.getItems().stream()
                .filter(item -> item.getMaterial() == material)
                .findFirst().orElse(null);
    }

    public static List<InfiniteChest> getInfiniteChests() {
        return new ArrayList<>(infiniteChests);
    }

    public static int getMaxSlots(Player player) {
        final FileConfiguration config = InfinityChest.get().getConfig();
        return Math.min(config.getInt("main-menu.size", 27), player.getEffectivePermissions().stream()
                .filter(PermissionAttachmentInfo::getValue)
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("infinitychest.slots."))
                .map(permission -> {
                    try {
                        return Integer.parseInt(permission.replace("infinitychest.slots.", ""));
                    } catch (NumberFormatException ex) {
                        return 0;
                    }
                })
                .max(Comparator.naturalOrder())
                .orElse(0));
    }

    public static boolean isBannedItem(ItemStack itemStack) {
        final FileConfiguration config = InfinityChest.get().getConfig();
        if (config.getStringList("blacklisted-items").stream()
                .anyMatch(item -> item.equalsIgnoreCase(itemStack.getType().name())))
            return true;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return false;
        if (itemMeta.hasDisplayName())
            return true;
        if (itemMeta.hasLore())
            return true;
        if (itemMeta.hasAttributeModifiers())
            return true;
        if (itemMeta.hasCustomModelData())
            return true;
        return itemMeta.hasEnchants();
    }
}
