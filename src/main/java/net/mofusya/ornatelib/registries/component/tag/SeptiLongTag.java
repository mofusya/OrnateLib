package net.mofusya.ornatelib.registries.component.tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.world.item.ItemStack;
import net.mofusya.ornatelib.lang.SeptiLong;
import net.mofusya.ornatelib.util.function.Modification;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Deprecated(since = "1.20.1-alpha0.8a-forge")
public class SeptiLongTag extends Tags {
    public SeptiLongTag(String id) {
        super(id);
    }

    @NotNull
    public SeptiLong get(ItemStack itemStack) {
        String jsonString = itemStack.getOrCreateTag().getString(modId + ":" + id);
        if (jsonString.isBlank()) return new SeptiLong();
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        return SeptiLong.createFromList(jsonArray.asList().stream().map(JsonElement::getAsLong).toList());
    }

    public void set(ItemStack itemStack, @NotNull SeptiLong septiLong) {
        JsonArray jsonArray = new JsonArray(SeptiLong.LAYER_SIZE);
        Arrays.stream(septiLong.getLayer()).forEach(jsonArray::add);
        itemStack.getOrCreateTag().putString(modId + ":" + id, jsonArray.toString());
    }

    public void set(ItemStack itemStack) {
        this.set(itemStack, new SeptiLong());
    }

    public void modify(ItemStack itemStack, Modification<SeptiLong> modification) {
        this.set(itemStack, modification.apply(this.get(itemStack)));
    }

    public void add(ItemStack itemStack, long add) {
        this.modify(itemStack, septiLong -> septiLong.add(add));
    }

    public void add(ItemStack itemStack, SeptiLong add) {
        this.modify(itemStack, septiLong -> septiLong.add(add));
    }

    public void remove(ItemStack itemStack, long remove) {
        this.modify(itemStack, septiLong -> septiLong.remove(remove));
    }

    public void remove(ItemStack itemStack, SeptiLong remove) {
        this.modify(itemStack, septiLong -> septiLong.remove(remove));
    }
}
