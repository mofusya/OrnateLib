package net.mofusya.ornatelib.registries.component.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class JsonTag extends Tags{
    public JsonTag(String id) {
        super(id);
    }

    @NotNull
    public JsonElement get(ItemStack itemStack) {
        String jsonString = itemStack.getOrCreateTag().getString(MOD_ID + ":" + ID);
        if (jsonString.isBlank()) return new JsonObject();
        return JsonParser.parseString(jsonString);
    }

    public void set(ItemStack itemStack, @NotNull JsonElement jsonElement) {
        itemStack.getOrCreateTag().putString(MOD_ID + ":" + ID, jsonElement.toString());
    }
}
