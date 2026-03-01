package net.mofusya.ornatelib.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FoilItem extends Item {
    public FoilItem(Properties build) {
        super(build);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
