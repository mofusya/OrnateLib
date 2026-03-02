package net.mofusya.ornatelib.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class FixedPickaxeItem extends PickaxeItem {
    public FixedPickaxeItem(Tier tier, int attackDamage, float attackSpeed, Properties build) {
        this(tier, attackDamage, attackSpeed, build, false);
    }

    public FixedPickaxeItem(Tier tier, int attackDamage, float attackSpeed, Properties build, boolean fixToSword) {
        super(tier, fixToSword ? attackDamage / 3 * 2 - 1 : attackDamage - 1, attackSpeed + (fixToSword ? -4.4f : -4f), build);
    }
}