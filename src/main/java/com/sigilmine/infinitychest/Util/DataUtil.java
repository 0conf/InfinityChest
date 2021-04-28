package com.sigilmine.infinitychest.Util;

import com.google.gson.*;
import com.sigilmine.infinitychest.InfinityChest;
import com.sigilmine.infinitychest.Entities.InfiniteChest;
import org.bukkit.entity.Player;

import java.io.*;

/**
 * @author Hyronical (hyro#8517)
 */
public class DataUtil {

    public static void updateInfiniteChest(InfiniteChest infiniteChest) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(InfiniteChest.class, new ChestAdapter())
                .create();
        String uuid = infiniteChest.getPlayer().getUniqueId().toString();
        final File playerFile = new File(InfinityChest.get().getDataFolder() + "/data", uuid + ".json");
        String jsonStr = gson.toJson(infiniteChest);

        try {
            FileWriter file = new FileWriter(playerFile);
            file.write(jsonStr);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static InfiniteChest getInfiniteChest(Player player) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(InfiniteChest.class, new ChestAdapter()).create();
        final File playerFile = new File(InfinityChest.get().getDataFolder() + "/data", player.getUniqueId().toString() + ".json");

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new InfiniteChest(player);
        }
        InfiniteChest ic = null;
        try {
            ic = gson.fromJson(new FileReader(playerFile), InfiniteChest.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ic;
    }

}
