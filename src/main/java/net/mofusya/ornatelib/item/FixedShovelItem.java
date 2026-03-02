package net.mofusya.ornatelib.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class FixedShovelItem extends ShovelItem {
    public FixedShovelItem(Tier tier, float attackDamage, float attackSpeed, Properties build) {
        this(tier, attackDamage, attackSpeed, build, false);
    }

    public FixedShovelItem(Tier tier, float attackDamage, float attackSpeed, Properties build, boolean fixToSword) {
        super(tier, fixToSword ? attackDamage / 3f * 2f - 0.5f : attackDamage - 1f, attackSpeed + (fixToSword ? -4.6f : -4f), build);
    }
}