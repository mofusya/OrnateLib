package net.mofusya.ornatelib.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class OrnateBlockDeferredRegister {

    private final ArrayList<DeferredRegister<Block>> blockRegisters = new ArrayList<>();
    private final OrnateItemDeferredRegister itemRegisters;

    private OrnateBlockDeferredRegister(String modId, int blockRegisterSlotCount) {
        this.itemRegisters = OrnateItemDeferredRegister.create(modId, blockRegisterSlotCount);
        for (int i = 0; i < blockRegisterSlotCount; i++) {
            this.blockRegisters.add(DeferredRegister.create(ForgeRegistries.BLOCKS, modId));
        }
    }

    public RegistryObject<Block> register(String id) {
        return this.register(id, Block::new, 0);
    }

    public RegistryObject<Block> register(String id, int slot) {
        return this.register(id, Block::new, BlockBehaviour.Properties.of(), new Item.Properties(), slot);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block) {
        return this.register(id, block, 0);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, int slot) {
        return this.register(id, block, BlockBehaviour.Properties.of(), new Item.Properties(), slot);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, Item.Properties itemBuild) {
        return this.register(id, block, itemBuild, 0);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, Item.Properties itemBuild, int slot) {
        return this.register(id, block, BlockBehaviour.Properties.of(), itemBuild, slot);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties blockBuild) {
        return this.register(id, block, blockBuild, 0);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties blockBuild, int slot) {
        return this.register(id, block, blockBuild, new Item.Properties(), slot);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties blockBuild, Item.Properties itemBuild) {
        return this.register(id, block, blockBuild, itemBuild, 0);
    }

    public RegistryObject<Block> register(String id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties blockBuild, Item.Properties itemBuild, int slot) {
        RegistryObject<Block> toReturn = this.blockRegisters.get(slot).register(id, () -> block.apply(blockBuild));
        this.itemRegisters.register(id, () -> new BlockItem(toReturn.get(), itemBuild), slot);
        return toReturn;
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block) {
        return this.register(id, block, new Item.Properties());
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block, int slot) {
        return this.register(id, block, new Item.Properties(), slot);
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block, Item.Properties itemBuild) {
        return this.register(id, block, itemBuild, 0);
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block, Item.Properties itemBuild, int slot) {
        RegistryObject<Block> toReturn = this.blockRegisters.get(slot).register(id, block);
        this.itemRegisters.register(id, () -> new BlockItem(toReturn.get(), itemBuild), slot);
        return toReturn;
    }

    public void register(IEventBus eventBus) {
        this.blockRegisters.forEach(deferredRegister -> deferredRegister.register(eventBus));
        this.itemRegisters.register(eventBus);
    }

    public DeferredRegister<Item> getItemRegister() {
        return this.itemRegisters.getItemRegister();
    }

    public DeferredRegister<Item> getItemRegister(int slot) {
        return this.itemRegisters.getItemRegister(slot);
    }

    public List<RegistryObject<Item>> getMainItems() {
        return this.itemRegisters.getMainItems();
    }

    public List<RegistryObject<Item>> getItems() {
        return this.itemRegisters.getItems();
    }

    public List<RegistryObject<Item>> getItems(int slot) {
        return this.itemRegisters.getItems(slot);
    }

    public List<RegistryObject<Item>> getItems(int... slots) {
        return this.itemRegisters.getItems(slots);
    }

    public DeferredRegister<Block> getBlockRegister() {
        return this.getBlockRegister(0);
    }

    public DeferredRegister<Block> getBlockRegister(int slot) {
        return this.blockRegisters.get(slot);
    }

    public List<RegistryObject<Block>> getMainBlocks() {
        return this.getBlocks(0);
    }

    public List<RegistryObject<Block>> getBlocks() {
        List<RegistryObject<Block>> toReturn = new ArrayList<>();
        for (DeferredRegister<Block> register : this.blockRegisters) {
            toReturn.addAll(register.getEntries());
        }
        return toReturn;
    }

    public List<RegistryObject<Block>> getBlocks(int slot) {
        return new ArrayList<>(this.getBlockRegister(slot).getEntries());
    }

    public List<RegistryObject<Block>> getBlocks(int... slots) {
        List<RegistryObject<Block>> toReturn = new ArrayList<>();
        for (int slot : slots) {
            toReturn.addAll(this.getBlockRegister(slot).getEntries());
        }
        return toReturn;
    }

    public static OrnateBlockDeferredRegister create(String modId) {
        return new OrnateBlockDeferredRegister(modId, 1);
    }

    public static OrnateBlockDeferredRegister create(String modId, int itemRegisterSlotCount) {
        return new OrnateBlockDeferredRegister(modId, itemRegisterSlotCount);
    }
}