package net.mofusya.ornatelib.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

public class FixedHoeItem extends HoeItem {
    public FixedHoeItem(Tier tier, int attackDamage, float attackSpeed, Properties build) {
        this(tier, attackDamage, attackSpeed, build, false);
    }

    public FixedHoeItem(Tier tier, int attackDamage, float attackSpeed, Properties build, boolean fixToSword) {
        super(tier, attackDamage + (fixToSword ? -6 : -4), fixToSword ? (attackSpeed - 0.1f) * 2f - 4f : attackSpeed - 4f, build);
    }
}
