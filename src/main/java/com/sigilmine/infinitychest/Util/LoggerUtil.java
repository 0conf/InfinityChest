package com.sigilmine.infinitychest.Util;

import org.bukkit.Bukkit;

public class LoggerUtil {
    public enum LogLevel {ERROR, WARNING, SUCCESS, DEBUG, ACTION, GENERIC, BLANK}

    public static void log(LogLevel level, String msg) {
        if (msg == null) return;

        switch (level) {
            case ERROR:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&4[InfinityChest - Error] &c" + msg));
                break;
            case WARNING:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&6[InfinityChest - Warning] &e" + msg));
                break;
            case SUCCESS:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&2[InfinityChest - Success] &a" + msg));
                break;
            case DEBUG:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&5[InfinityChest - Debug] &d" + msg));
                break;
            case ACTION:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&3[InfinityChest - Log] &b" + msg));
                break;
            case GENERIC:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&3[InfinityChest] &b" + msg));
                break;
            case BLANK:
                Bukkit.getConsoleSender().sendMessage(MessageUtil.format(msg));
                break;
        }
    }
}
