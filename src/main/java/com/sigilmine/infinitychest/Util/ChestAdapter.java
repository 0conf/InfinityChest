package com.sigilmine.infinitychest.Util;

import com.google.gson.*;
import com.sigilmine.infinitychest.Entities.InfiniteChest;
import com.sigilmine.infinitychest.Entities.InfiniteItem;
import com.sigilmine.infinitychest.InfinityChest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Hyronical (hyro#8517)
 */
public class ChestAdapter implements JsonSerializer<InfiniteChest>, JsonDeserializer<InfiniteChest> {

    @Override
    public JsonElement serialize(InfiniteChest infiniteChest, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.add("player", new JsonPrimitive(infiniteChest.getPlayer().getUniqueId().toString()));
        obj.add("autoCollect", new JsonPrimitive(infiniteChest.isAutoCollect()));

        JsonArray items = new JsonArray();
        for (InfiniteItem item : infiniteChest.getItems()) {
            JsonObject itemObj = new JsonObject();
            itemObj.add("material",  new JsonPrimitive(item.getMaterial().toString()));
            itemObj.add("amount",  new JsonPrimitive(item.getAmount()));
            items.add(itemObj);
        }
        obj.add("items", items);
        return obj;
    }

    @Override
    public InfiniteChest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonArray itemsObj = obj.getAsJsonArray("items");

        boolean autoCollect = obj.get("autoCollect").getAsBoolean();
        String uuid = obj.get("player").getAsString();
        Player player = InfinityChest.get().getServer().getPlayer(UUID.fromString(uuid));

        List<InfiniteItem> items = new ArrayList<>();

        for (JsonElement e : itemsObj) {
            JsonObject io = e.getAsJsonObject();
            String material = io.get("material").getAsString();
            long amount = io.get("amount").getAsLong();
            InfiniteItem infiniteItem = new InfiniteItem(Material.matchMaterial(material), amount);
            items.add(infiniteItem);
        }

        return new InfiniteChest(player, items, autoCollect);
    }
}
