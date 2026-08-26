package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;

public class StringTag extends Tags {
    public StringTag(String id) {
        super(id);
    }

    public String get(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getString(modId + ":" + id);
    }

    public void set(ItemStack itemStack, String i) {
        itemStack.getOrCreateTag().putString(modId + ":" + id, i);
    }

    public void set(ItemStack itemStack) {
        set(itemStack, "");
    }
}