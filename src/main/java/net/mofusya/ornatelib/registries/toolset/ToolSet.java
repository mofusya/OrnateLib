package net.mofusya.ornatelib.registries.toolset;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.item.*;
import net.mofusya.ornatelib.registries.OrnateItemDeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ToolSet {
    private final TagKey<Block> requiresThisTool;
    private final Tier toolTier;
    private final ArrayList<RegistryObject<Item>> items = new ArrayList<>();

    public ToolSet(OrnateItemDeferredRegister register, String modId, String id, Builder build, int slot) {
        this.requiresThisTool = BlockTags.create(new ResourceLocation(modId, "needs_" + id + "_tool"));
        this.toolTier = TierSortingRegistry.registerTier(new ForgeTier(build.toolLevel, build.durability, build.digSpeed, 0f, build.enchantmentValue, this.requiresThisTool,
                () -> Ingredient.of(build.ingredient)), new ResourceLocation(modId, id), List.of(build.strongerThan), List.of());
        this.items.add(register.register(id + "_sword", new FixedSwordItem(this.toolTier, build.attackDamage, build.attackSpeed, build.property), slot));
        this.items.add(register.register(id + "_axe", new FixedAxeItem(this.toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        this.items.add(register.register(id + "_pickaxe", new FixedPickaxeItem(this.toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        this.items.add(register.register(id + "_shovel", new FixedShovelItem(this.toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        this.items.add(register.register(id + "_hoe", new FixedHoeItem(this.toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
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
        private ItemLike ingredient;
        private int attackDamage = 6;
        private float attackSpeed = 1.2f;
        private int toolLevel = 5;
        private float digSpeed = 9f;
        private int durability = 1024;
        private int enchantmentValue = 30;
        private Tier strongerThan = Tiers.NETHERITE;
        private Item.Properties property = new Item.Properties();

        private Builder(ItemLike ingredient) {
            this.ingredient = ingredient;
        }

        public Builder ingredient(ItemLike ingredient) {
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

        public Builder strongerThan(Tier strongerThan){
            this.strongerThan = strongerThan;
            return this;
        }

        public Builder property(Item.Properties property) {
            this.property = property;
            return this;
        }

        public static Builder create(ItemLike ingredient) {
            return new Builder(ingredient);
        }
    }
}