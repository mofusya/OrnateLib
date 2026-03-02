package net.mofusya.ornatelib.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public class FixedAxeItem extends AxeItem {
    public FixedAxeItem(Tier tier, float attackDamage, float attackSpeed, Properties build) {
        this(tier, attackDamage, attackSpeed, build, false);
    }

    public FixedAxeItem(Tier tier, float attackDamage, float attackSpeed, Properties build, boolean fixToSword) {
        super(tier, attackDamage + (fixToSword ? -3f : -1f), attackSpeed + (fixToSword ? -4.7f : -4f), build);
    }
}
