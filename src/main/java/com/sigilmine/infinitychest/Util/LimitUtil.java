package com.sigilmine.infinitychest.Util;

import com.sigilmine.infinitychest.InfinityChest;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class LimitUtil {

    private static HashMap<UUID, Long> limits = new HashMap<>();
    private static long limitTime = 0;

    public static long getLimit(Player player) {
        return limits.getOrDefault(player.getUniqueId(), 0L);
    }

    public static void setLimit(Player player, long limit) {
        limits.put(player.getUniqueId(), limit);
    }

    public static void resetLimits() {
        limits = new HashMap<>();
    }
    public static long getLimitResetTime() {
        return limitTime;
    }

    public static int getMaxSellAmount(Player player) {
        return player.getEffectivePermissions().stream()
                .filter(PermissionAttachmentInfo::getValue)
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("infinitychest.sellamount."))
                .map(permission -> {
                    try {
                        return Integer.parseInt(permission.replace("infinitychest.sellamount.", ""));
                    } catch (NumberFormatException ex) {
                        return 0;
                    }
                })
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

}
