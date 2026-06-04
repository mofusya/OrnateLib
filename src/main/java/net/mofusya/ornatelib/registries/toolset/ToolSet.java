package net.mofusya.ornatelib.registries.toolset;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.item.*;
import net.mofusya.ornatelib.util.function.QuadFunction;
import net.mofusya.ornatelib.util.function.QuintFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ToolSet {
    private final TagKey<Block> requiresThisTool;
    private final Tier toolTier;
    private final ArrayList<RegistryObject<Item>> items;

    public ToolSet(TagKey<Block> requiresThisTool, Tier toolTier, List<RegistryObject<Item>> items) {
        this.requiresThisTool = requiresThisTool;
        this.toolTier = toolTier;
        this.items = new ArrayList<>(items);
    }

    public RegistryObject<Item> getItem(Type type) {
        return this.items.get(type.ordinal());
    }

    public TagKey<Block> getRequiresThisTool() {
        return this.requiresThisTool;
    }

    public Tier getToolTier() {
        return this.toolTier;
    }

    public enum Type {
        SWORD, AXE, PICKAXE, SHOVEL, HOE
    }

    public static class Builder {
        private Supplier<ItemLike> ingredient;
        private int attackDamage = 6;
        private float attackSpeed = 1.6f;
        private int toolLevel = 5;
        private float digSpeed = 9f;
        private int durability = 1024;
        private int enchantmentValue = 30;
        private Tier strongerThan = Tiers.NETHERITE;
        private Item.Properties property = new Item.Properties();
        private QuadFunction<Tier, Integer, Float, Item.Properties, FixedSwordItem> swordFunc = FixedSwordItem::new;
        private QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedAxeItem> axeFunc = FixedAxeItem::new;
        private QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedPickaxeItem> pickaxeFunc = FixedPickaxeItem::new;
        private QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedShovelItem> shovelFunc = FixedShovelItem::new;
        private QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedHoeItem> hoeFunc = FixedHoeItem::new;

        public Builder(Supplier<ItemLike> ingredient) {
            this.ingredient = ingredient;
        }

        public Builder ingredient(Supplier<ItemLike> ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public Builder attackDamage(int attackDamage) {
            this.attackDamage = attackDamage;
            return this;
        }

        public Builder attackSpeed(float attackSpeed) {
            this.attackSpeed = attackSpeed;
            return this;
        }

        public Builder toolLevel(int toolLevel) {
            this.toolLevel = toolLevel;
            return this;
        }

        public Builder digSpeed(float digSpeed) {
            this.digSpeed = digSpeed;
            return this;
        }

        public Builder durability(int durability) {
            this.durability = durability;
            return this;
        }

        public Builder enchantmentValue(int enchantmentValue) {
            this.enchantmentValue = enchantmentValue;
            return this;
        }

        public Builder strongerThan(Tier strongerThan) {
            this.strongerThan = strongerThan;
            return this;
        }

        public Builder property(Item.Properties property) {
            this.property = property;
            return this;
        }

        public Builder swordFunc(QuadFunction<Tier, Integer, Float, Item.Properties, FixedSwordItem> swordFunc) {
            this.swordFunc = swordFunc;
            return this;
        }

        public Builder axeFunc(QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedAxeItem> axeFunc) {
            this.axeFunc = axeFunc;
            return this;
        }

        public Builder pickaxeFunc(QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedPickaxeItem> pickaxeFunc) {
            this.pickaxeFunc = pickaxeFunc;
            return this;
        }

        public Builder shovelFunc(QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedShovelItem> shovelFunc) {
            this.shovelFunc = shovelFunc;
            return this;
        }

        public Builder hoeFunc(QuintFunction<Tier, Integer, Float, Item.Properties, Boolean, FixedHoeItem> hoeFunc) {
            this.hoeFunc = hoeFunc;
            return this;
        }

        public int getAttackDamage() {
            return this.attackDamage;
        }

        public float getAttackSpeed() {
            return this.attackSpeed;
        }

        public float getDigSpeed() {
            return this.digSpeed;
        }

        public int getDurability() {
            return this.durability;
        }

        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        public ItemLike getIngredient() {
            return this.ingredient.get();
        }

        public Item.Properties getProperty() {
            return this.property;
        }

        public Tier getStrongerThan() {
            return this.strongerThan;
        }

        public int getToolLevel() {
            return this.toolLevel;
        }

        public FixedSwordItem getSwordItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties build) {
            return this.swordFunc.apply(tier, attackDamage, attackSpeed, build);
        }

        public FixedAxeItem getAxeItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties build, boolean fixToSword) {
            return this.axeFunc.apply(tier, attackDamage, attackSpeed, build, fixToSword);
        }

        public FixedPickaxeItem getPickaxeItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties build, boolean fixToSword) {
            return this.pickaxeFunc.apply(tier, attackDamage, attackSpeed, build, fixToSword);
        }

        public FixedShovelItem getShovelItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties build, boolean fixToSword) {
            return this.shovelFunc.apply(tier, attackDamage, attackSpeed, build, fixToSword);
        }

        public FixedHoeItem getHoeItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties build, boolean fixToSword) {
            return this.hoeFunc.apply(tier, attackDamage, attackSpeed, build, fixToSword);
        }
    }
}