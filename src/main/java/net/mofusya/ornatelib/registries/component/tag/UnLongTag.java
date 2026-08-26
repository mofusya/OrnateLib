package net.mofusya.ornatelib.registries.component.tag;

import net.minecraft.world.item.ItemStack;
import net.mofusya.ornatelib.lang.UnLong;

import java.util.Arrays;
import java.util.function.Function;

public class UnLongTag extends Tags {
    public UnLongTag(String id) {
        super(id);
    }

    public UnLong get(ItemStack itemStack) {
        return new UnLong(Arrays.stream(itemStack.getOrCreateTag().getLongArray(this.id)).boxed().toList());
    }

    public void set(ItemStack itemStack, UnLong unLong) {
        itemStack.getOrCreateTag().putLongArray(this.id, unLong.getValues());
    }

    public void modify(ItemStack itemStack, Function<UnLong, UnLong> modify) {
        this.set(itemStack, modify.apply(this.get(itemStack)));
    }
}
