package com.sigilmine.infinitychest;

import com.sigilmine.infinitychest.commands.InfinityCMD;
import com.sigilmine.infinitychest.events.MenuEvents;
import com.sigilmine.infinitychest.events.PlayerEvents;
import com.sigilmine.infinitychest.files.DataFile;
import com.sigilmine.infinitychest.files.WorthFile;
import com.sigilmine.infinitychest.tasks.IntervalLimitResetTask;
import com.sigilmine.infinitychest.tasks.IntervalSaveTask;
import com.sigilmine.infinitychest.tasks.PlayerLoadTask;
import com.sigilmine.infinitychest.util.ChestUtil;
import com.sigilmine.infinitychest.util.DataUtil;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public final class InfinityChest extends JavaPlugin {
    private static InfinityChest instance;
    private static Economy economy;
    private static LuckPerms luckPerms;
    private DataFile dataFile;
    private WorthFile worthFile;

    private CommandSender cs = Bukkit.getConsoleSender();
    @Override
    public void onEnable() {
        instance = this;
        cs.sendMessage(ChatColor.DARK_AQUA + "____________________________________");
        cs.sendMessage(ChatColor.AQUA + " InfinityChest");
        cs.sendMessage(ChatColor.GRAY + " Version: " + this.getDescription().getVersion());
        cs.sendMessage(ChatColor.GRAY + " Developed by Ignitus Co.");
        cs.sendMessage(ChatColor.DARK_AQUA + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        saveDefaultConfig();

        if (!setupEconomy()) {
            cs.sendMessage(ChatColor.RED + "[InfinityChest] Vault not found. Disabling plugin...");
            getPluginLoader().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            cs.sendMessage(ChatColor.AQUA + "[InfinityChest] LuckPerms found. Enabling capabilities!");
            setupLuckPerms();
        } else {
            cs.sendMessage(ChatColor.YELLOW + "[InfinityChest] LuckPerms not found. Disabling capabilities!");
        }

        dataFile = new DataFile();
        worthFile = new WorthFile();

        new InfinityCMD(this);
        getServer().getPluginManager().registerEvents(new MenuEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        startTasks();
    }

    @Override
    public void onDisable() {
        cs.sendMessage(ChatColor.DARK_AQUA + "____________________________________");
        cs.sendMessage(ChatColor.AQUA + " Disabling InfinityChest...");
        cs.sendMessage(ChatColor.DARK_AQUA + "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        ChestUtil.getInfiniteChests().forEach(DataUtil::updateInfiniteChest);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private void setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null)
            luckPerms = provider.getProvider();
    }

    private void startTasks() {
        Bukkit.getOnlinePlayers().forEach(player -> new PlayerLoadTask(player).runTask(this));
        new IntervalSaveTask().runTaskTimer(this, (long) 20 * 60 * 5, (long) 20 * 60 * 5);

        final long resetLimit = getConfig().getLong("tasks.limit-reset", 2400L);
        new IntervalLimitResetTask().runTaskTimer(this, resetLimit, resetLimit);
    }

    public static InfinityChest get() {
        return instance;
    }
    public WorthFile getWorthFile() {
        return worthFile;
    }
    public DataFile getDataFile() {
        return dataFile;
    }
    public CommandSender getCs() {
        return cs;
    }
    public static Economy getEconomy() {
        return economy;
    }
    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
