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
import net.mofusya.ornatelib.item.AttributedItem;
import net.mofusya.ornatelib.registries.toolset.ToolSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OrnateItemRegister {

    private final ArrayList<DeferredRegister<Item>> itemRegisters = new ArrayList<>();
    private final String modId;

    public OrnateItemRegister(String modId) {
        this(modId, 1);
    }
    public OrnateItemRegister(String modId, int indexCount) {
        this.modId = modId;

        for (int i = 0; i < indexCount; i++) {
            this.itemRegisters.add(DeferredRegister.create(ForgeRegistries.ITEMS, modId));
        }
    }


    //  Item registering
    public RegistryObject<Item> register(String id) {
        return this.register(id, 0);
    }

    public RegistryObject<Item> register(String id, int index) {
        return this.register(id, Item::new, index);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item) {
        return this.register(id, item, 0);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, int index) {
        return this.itemRegisters.get(index).register(id, () -> item.apply(new Item.Properties()));
    }

    public RegistryObject<Item> register(String id, Item.Properties build) {
        return this.register(id, build, 0);
    }

    public RegistryObject<Item> register(String id, Item.Properties build, int index) {
        return this.register(id, Item::new, build, index);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, Item.Properties build) {
        return this.register(id, item, build, 0);
    }

    public RegistryObject<Item> register(String id, Function<Item.Properties, Item> item, Item.Properties build, int index) {
        return this.itemRegisters.get(index).register(id, () -> item.apply(build));
    }

    public RegistryObject<Item> register(String id, Supplier<Item> item) {
        return this.register(id, item, 0);
    }

    public RegistryObject<Item> register(String id, Supplier<Item> item, int index) {
        return this.itemRegisters.get(index).register(id, item);
    }

    //  Attributed item registering
    public RegistryObject<Item> register(String id, AttributedItem.Builder builder) {
        return this.register(id, builder, 0);
    }

    public RegistryObject<Item> register(String id, AttributedItem.Builder builder, int index) {
        return this.register(id, AttributedItem::new, builder, index);
    }

    public RegistryObject<Item> register(String id, BiFunction<Item.Properties, AttributedItem.Builder, AttributedItem> item, AttributedItem.Builder builder) {
        return this.register(id, item, builder, 0);
    }

    public RegistryObject<Item> register(String id, BiFunction<Item.Properties, AttributedItem.Builder, AttributedItem> item, AttributedItem.Builder builder, int index) {
        return this.register(id, item, new Item.Properties(), builder, index);
    }

    public RegistryObject<Item> register(String id, BiFunction<Item.Properties, AttributedItem.Builder, AttributedItem> item, Item.Properties build, AttributedItem.Builder builder) {
        return this.register(id, item, build, builder, 0);
    }

    public RegistryObject<Item> register(String id, BiFunction<Item.Properties, AttributedItem.Builder, AttributedItem> item, Item.Properties build, AttributedItem.Builder builder, int index) {
        return this.itemRegisters.get(index).register(id, () -> item.apply(build, builder));
    }

    //  ToolSet registering
    public ToolSet register(String id, ToolSet.Builder toolsetBuilder) {
        return this.register(id, toolsetBuilder, 0);
    }

    public ToolSet register(String id, ToolSet.Builder build, int index) {
        ArrayList<RegistryObject<Item>> items = new ArrayList<>();

        TagKey<Block> requiresThisTool = BlockTags.create(new ResourceLocation(modId, "needs_" + id + "_tool"));
        Tier toolTier = TierSortingRegistry.registerTier(new ForgeTier(build.getToolLevel(), build.getDurability(), build.getDigSpeed(), 0f, build.getEnchantmentValue(), requiresThisTool,
                () -> Ingredient.of(build.getIngredient())), new ResourceLocation(modId, id), List.of(build.getStrongerThan()), List.of());
        items.add(this.register(id + "_sword", () -> build.getSwordItem(toolTier, build.getAttackDamage(), build.getAttackSpeed(), build.getProperty()), index));
        items.add(this.register(id + "_axe", () -> build.getAxeItem(toolTier, build.getAttackDamage(), build.getAttackSpeed(), build.getProperty(), true), index));
        items.add(this.register(id + "_pickaxe", () -> build.getPickaxeItem(toolTier, build.getAttackDamage(), build.getAttackSpeed(), build.getProperty(), true), index));
        items.add(this.register(id + "_shovel", () -> build.getShovelItem(toolTier, build.getAttackDamage(), build.getAttackSpeed(), build.getProperty(), true), index));
        items.add(this.register(id + "_hoe", () -> build.getHoeItem(toolTier, build.getAttackDamage(), build.getAttackSpeed(), build.getProperty(), true), index));

        return new ToolSet(requiresThisTool, toolTier, items);
    }

    //  Eventbus register
    public void register(IEventBus eventBus) {
        this.itemRegisters.forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    //  Getter's and setter's
    public DeferredRegister<Item> getItemRegister() {
        return this.getItemRegister(0);
    }

    public DeferredRegister<Item> getItemRegister(int index) {
        return this.itemRegisters.get(index);
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

    public List<RegistryObject<Item>> getItems(int index) {
        return new ArrayList<>(this.getItemRegister(index).getEntries());
    }

    public List<RegistryObject<Item>> getItems(int... indexes) {
        List<RegistryObject<Item>> toReturn = new ArrayList<>();
        for (int slot : indexes) {
            toReturn.addAll(this.getItemRegister(slot).getEntries());
        }
        return toReturn;
    }
}