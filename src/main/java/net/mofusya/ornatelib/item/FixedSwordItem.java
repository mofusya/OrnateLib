package net.mofusya.ornatelib.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class FixedSwordItem extends SwordItem {
    public FixedSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties build) {
        super(tier, attackDamage - 1, attackSpeed - 4f, build);
    }
}
