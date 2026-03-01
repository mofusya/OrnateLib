package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;

public class FloatTag extends Tags {
    public FloatTag(String id) {
        super(id);
    }

    public float get(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(MOD_ID + ":" + ID);
    }

    public void set(ItemStack itemStack, float i) {
        itemStack.getOrCreateTag().putFloat(MOD_ID + ":" + ID, i);
    }

    public void set(ItemStack itemStack) {
        set(itemStack, 0f);
    }

    public void add(ItemStack itemStack, float i) {
        set(itemStack, get(itemStack) + i);
    }

    public void add(ItemStack itemStack) {
        add(itemStack, 1f);
    }

    public void remove(ItemStack itemStack, float i) {
        add(itemStack, -i);
    }

    public void remove(ItemStack itemStack) {
        remove(itemStack, 1f);
    }

    public void reverse(ItemStack itemStack) {
        set(itemStack, -get(itemStack));
    }
}