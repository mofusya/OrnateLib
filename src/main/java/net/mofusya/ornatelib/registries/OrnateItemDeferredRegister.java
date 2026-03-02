package net.mofusya.ornatelib.registries;

import com.google.common.base.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.item.*;
import net.mofusya.ornatelib.registries.toolset.ToolSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrnateItemDeferredRegister {

    private final ArrayList<DeferredRegister<Item>> itemRegisters = new ArrayList<>();
    private final String modId;

    private OrnateItemDeferredRegister(String modId, int itemRegisterSlotCount) {
        this.modId = modId;

        for (int i = 0; i < itemRegisterSlotCount; i++) {
            this.itemRegisters.add(DeferredRegister.create(ForgeRegistries.ITEMS, modId));
        }
    }

    public RegistryObject<Item> register(String id) {
        return this.register(id, Item::new, 0);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item) {
        return this.register(id, item, 0);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, int slot) {
        return this.itemRegisters.get(slot).register(id, () -> item.apply(new Item.Properties()));
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, Item.Properties build) {
        return this.register(id, item, build, 0);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, Item.Properties build, int slot) {
        return this.itemRegisters.get(slot).register(id, () -> item.apply(build));
    }

    public RegistryObject<Item> register(String id, Supplier<Item> item) {
        return this.register(id, item, 0);
    }

    public RegistryObject<Item> register(String id, Supplier<Item> item, int slot) {
        return this.itemRegisters.get(slot).register(id, item);
    }

    public ToolSet register(String id, ToolSet.Builder toolsetBuilder) {
        return this.register(id, toolsetBuilder, 0);
    }

    public ToolSet register(String id, ToolSet.Builder build, int slot) {
        ArrayList<RegistryObject<Item>> items = new ArrayList<>();

        TagKey<Block> requiresThisTool = BlockTags.create(new ResourceLocation(modId, "needs_" + id + "_tool"));
        Tier toolTier = TierSortingRegistry.registerTier(new ForgeTier(build.toolLevel, build.durability, build.digSpeed, 0f, build.enchantmentValue, requiresThisTool,
                () -> Ingredient.of(build.ingredient)), new ResourceLocation(modId, id), List.of(build.strongerThan), List.of());
        items.add(this.register(id + "_sword", () -> new FixedSwordItem(toolTier, build.attackDamage, build.attackSpeed, build.property), slot));
        items.add(this.register(id + "_axe", () -> new FixedAxeItem(toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        items.add(this.register(id + "_pickaxe", () -> new FixedPickaxeItem(toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        items.add(this.register(id + "_shovel", () -> new FixedShovelItem(toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));
        items.add(this.register(id + "_hoe", () -> new FixedHoeItem(toolTier, build.attackDamage, build.attackSpeed, build.property, true), slot));

        return new ToolSet(requiresThisTool, toolTier, items);
    }

    public void register(IEventBus eventBus) {
        this.itemRegisters.forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    public DeferredRegister<Item> getItemRegister() {
        return this.getItemRegister(0);
    }

    public DeferredRegister<Item> getItemRegister(int slot) {
        return this.itemRegisters.get(slot);
    }

    public List<RegistryObject<Item>> getMainItems() {
        return this.getItems(0);
    }

    public List<RegistryObject<Item>> getItems() {
        List<RegistryObject<Item>> toReturn = new ArrayList<>();
        for (DeferredRegister<Item> register : this.itemRegisters) {
            toReturn.addAll(register.getEntries());
        }
        return toReturn;
    }

    public List<RegistryObject<Item>> getItems(int slot) {
        return new ArrayList<>(this.getItemRegister(slot).getEntries());
    }

    public List<RegistryObject<Item>> getItems(int... slots) {
        List<RegistryObject<Item>> toReturn = new ArrayList<>();
        for (int slot : slots) {
            toReturn.addAll(this.getItemRegister(slot).getEntries());
        }
        return toReturn;
    }

    public static OrnateItemDeferredRegister create(String modId) {
        return new OrnateItemDeferredRegister(modId, 1);
    }

    public static OrnateItemDeferredRegister create(String modId, int itemRegisterSlotCount) {
        return new OrnateItemDeferredRegister(modId, itemRegisterSlotCount);
    }
}