package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;

public class IntegerListTag extends Tags {
    public IntegerListTag(String id) {
        super(id);
    }

    public int[] get(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getIntArray(MOD_ID + ":" + ID);
    }

    public int get(ItemStack itemStack, int dic) {
        int[] list = get(itemStack);

        return list[dic];
    }

    public void set(ItemStack itemStack, int i, int dic) {
        int[] list = get(itemStack);

        list[dic] = i;
        itemStack.getOrCreateTag().putIntArray(MOD_ID + ":" + ID, list);
    }

    public void set(ItemStack itemStack, int[] list) {
        itemStack.getOrCreateTag().putIntArray(MOD_ID + ":" + ID, list);
    }

    public void set(ItemStack itemStack, int dic) {
        set(itemStack, new int[dic]);
    }

    public void add(ItemStack itemStack, int i, int dic) {
        int[] list = get(itemStack);

        set(itemStack, list[dic] + i, dic);
    }

    public void add(ItemStack itemStack, int dic) {
        add(itemStack, 1, dic);
    }

    public void remove(ItemStack itemStack, int i, int dic) {
        add(itemStack, -i, dic);
    }

    public void remove(ItemStack itemStack, int dic) {
        remove(itemStack, 1, dic);
    }

    public void reverse(ItemStack itemStack, int dic) {
        int[] list = get(itemStack);

        set(itemStack, -list[dic], dic);
    }
}
