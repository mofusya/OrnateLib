package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;

public class IntegerTag extends Tags {
    public IntegerTag(String id) {
        super(id);
    }

    public int get(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getInt(MOD_ID + ":" + ID);
    }

    public void set(ItemStack itemStack, int i) {
        itemStack.getOrCreateTag().putInt(MOD_ID + ":" + ID, i);
    }

    public void set(ItemStack itemStack) {
        set(itemStack, 0);
    }

    public void add(ItemStack itemStack, int i) {
        set(itemStack, get(itemStack) + i);
    }

    public void add(ItemStack itemStack) {
        add(itemStack, 1);
    }

    public void remove(ItemStack itemStack, int i) {
        add(itemStack, -i);
    }

    public void remove(ItemStack itemStack) {
        remove(itemStack, 1);
    }

    public void reverse(ItemStack itemStack) {
        set(itemStack, -get(itemStack));
    }
}