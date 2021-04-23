package com.sigilmine.infinitychest.util;

import com.sigilmine.infinitychest.InfinityChest;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MessageUtil {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String format(String input, Object... strings) {
        String message = format(input);
        for (int i = 0; i < strings.length; i += 2)
            message = message.replace(strings[i].toString(), strings[i + 1].toString());
        return message;
    }

    public static List<String> format(List<String> input, Object... replace) {
        return input.stream().map(line -> format(line, replace)).collect(Collectors.toList());
    }

    public static String getMessage(String path, Object... replace) {
        return format(InfinityChest.get().getConfig().getString("messages." + path, "&cUnknown message"), replace);
    }

    public static String getFriendlyMaterialName(Material material) {
        return material.toString().toLowerCase().replaceAll("_", " ");
    }
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatDouble(double value) {
        String text = Double.toString(Math.abs(value));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        return new DecimalFormat("0.00" + (decimalPlaces <= 2 ? "" : "0")).format(value);
    }

    public static String longToString(long value) {
        if (value == Long.MIN_VALUE) return longToString(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + longToString(-value);
        if (value < 1000) return Long.toString(value);
        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
