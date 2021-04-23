package com.sigilmine.infinitychest.files;

import com.sigilmine.infinitychest.InfinityChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author DDarkInferno (Ignitus Co.)
 */
public class FileManager {

    private InfinityChest main = InfinityChest.get();
    private String fileName;
    private FileConfiguration fileConfiguration;
    private File file;

    public FileManager(String fileName) {
        this.fileName = fileName;
        saveDefaultConfig();
        reloadConfig();
    }

    private void saveDefaultConfig() {
        if (file == null) {
            file = new File(main.getDataFolder(), fileName);
        }
        if (!file.exists()) {
            main.saveResource(fileName, false);
        }
    }

    public void reloadConfig() {
        if (file == null) {
            file = new File(main.getDataFolder(), fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileConfiguration() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }


    public void saveFileConfiguration() {
        if (fileConfiguration == null || file == null) {
            return;
        }
        try {
            getFileConfiguration().save(file);
        } catch (final IOException ex) {
            main.getLogger().severe("Could not save config to " + file);
        }
    }


}
