package com.sigilmine.infinitychest.entities;

import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.files.WorthFile;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;


public class InfiniteItem {

    private static WorthFile worthFile = InfinityChest.get().getWorthFile();

    private final Material material;
    private long amount;

    public InfiniteItem(Material material) {
        this.material = material;
        this.amount = 0;
    }


    public InfiniteItem(Material material, long amount) {
        this.material = material;
        this.amount = amount;
    }

    public double getSingleValue() {
        final FileConfiguration worthConfig = worthFile.getFileConfiguration();
        final ConfigurationSection section = worthConfig.getConfigurationSection("worth");
        if (section == null)
            return -1;
        String path = section.getKeys(false).stream()
                .filter(item -> item.equalsIgnoreCase(material.name()))
                .findFirst().orElse(null);
        if (path == null)
            return -1;
        return worthConfig.getDouble("worth." + path);
    }

    public double getTotalValue() {
        double singleValue = getSingleValue();
        if (singleValue <= 0.0)
            return 0;
        return singleValue * amount;
    }

    public Material getMaterial() {
        return this.material;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amt) {
        this.amount = amt;
    }

}
