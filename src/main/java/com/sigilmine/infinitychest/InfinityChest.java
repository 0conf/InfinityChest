package com.sigilmine.infinitychest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sigilmine.infinitychest.Commands.InfinityCMD;
import com.sigilmine.infinitychest.Entities.InfiniteChest;
import com.sigilmine.infinitychest.Events.MenuEvents;
import com.sigilmine.infinitychest.Events.PlayerEvents;
import com.sigilmine.infinitychest.Files.WorthFile;
import com.sigilmine.infinitychest.Tasks.IntervalLimitResetTask;
import com.sigilmine.infinitychest.Tasks.IntervalSaveTask;
import com.sigilmine.infinitychest.Tasks.PlayerLoadTask;
import com.sigilmine.infinitychest.Util.ChestAdapter;
import com.sigilmine.infinitychest.Util.ChestUtil;
import com.sigilmine.infinitychest.Util.DataUtil;
import com.sigilmine.infinitychest.Util.LoggerUtil;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;

/**
 * @author DDarkInferno (Ignitus Co.)
 * @author Hyronical (hyro#8517)
 */
public final class InfinityChest extends JavaPlugin {
    private static InfinityChest instance;
    private static Economy economy;
    private static LuckPerms luckPerms;
    private WorthFile worthFile;


    @Override
    public void onEnable() {
        instance = this;
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&3____________________________________");
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &bInfinityChest");
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &7Version: " + this.getDescription().getVersion());
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &7Developed by Ignitus Co. & Hyronical");
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&3‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        saveDefaultConfig();
        File file = new File(this.getDataFolder() + "/data");
        if (!file.exists()) {
            file.mkdir();
        }
        checkForClean();

        if (!setupEconomy()) {
            LoggerUtil.log(LoggerUtil.LogLevel.ERROR, "Vault not installed. Disabling plugin...");
            getPluginLoader().disablePlugin(this);
            return;
        }
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &bHooked into Vault!");
        if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &bHooked into LuckPerms!");
            setupLuckPerms();
        } else {
            LoggerUtil.log(LoggerUtil.LogLevel.BLANK, " &cLuckPerms not found. Disabling capability!");
        }

        worthFile = new WorthFile();

        new InfinityCMD(this);
        getServer().getPluginManager().registerEvents(new MenuEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        startTasks();
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&3‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
    }

    @Override
    public void onDisable() {
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&3____________________________________");
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&b Disabling InfinityChest...");
        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&3‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
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
        new IntervalSaveTask().runTaskTimerAsynchronously(this, (long) 20 * 60 * 5, (long) 20 * 60 * 5);

        final long resetLimit = getConfig().getLong("tasks.limit-reset", 2400L);
        new IntervalLimitResetTask().runTaskTimer(this, resetLimit, resetLimit);
    }

    public void checkForClean() {
        if (LocalDate.now().getDayOfMonth() == 1) {
            LoggerUtil.log(LoggerUtil.LogLevel.WARNING, "Initiated a file cleanup! All inactive/empty InfiniteChests will be deleted. This may take a while...");
            File folder = new File(this.getDataFolder() + "/data");
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(InfiniteChest.class, new ChestAdapter()).create();
            int count = 0;
            for (File fileEntry : folder.listFiles()) {
                try {
                    InfiniteChest ic = gson.fromJson(new FileReader(fileEntry), InfiniteChest.class);
                    if (ic.getItems().size() == 0 && !ic.isAutoCollect()) {
                        fileEntry.delete();
                        LoggerUtil.log(LoggerUtil.LogLevel.BLANK, "&6[Cleanup] &eSuccessfully removed &6" + fileEntry.getName());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                count++;
            }
            LoggerUtil.log(LoggerUtil.LogLevel.SUCCESS, "Successfully cleaned " + count + " empty chests!");
        }
    }



    public static InfinityChest get() {
        return instance;
    }
    public WorthFile getWorthFile() {
        return worthFile;
    }
    public static Economy getEconomy() {
        return economy;
    }
    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
