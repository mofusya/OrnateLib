package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;

public class BooleanTag extends Tags {
    public BooleanTag(String id) {
        super(id);
    }

    public boolean get(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getBoolean(modId + ":" + id);
    }

    public void set(ItemStack itemStack, boolean i) {
        itemStack.getOrCreateTag().putBoolean(modId + ":" + id, i);
    }

    public void trigger(ItemStack itemStack) {
        set(itemStack, !get(itemStack));
    }
}
