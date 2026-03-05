package net.mofusya.ornatelib.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.item.AttributedBlockItem;
import net.mofusya.ornatelib.item.AttributedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
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

    //  Block registering
    public RegistryObject<Block> register(String id, Builder builder) {
        return this.register(id, builder, 0);
    }

    public RegistryObject<Block> register(String id, Builder builder, int slot) {
        if (builder.block == null) {
            if (builder.blockFunc == null) builder.blockFunc = Block::new;
            if (builder.blockBuild == null)
                builder.blockBuild = builder.blockBuildRef == null ? BlockBehaviour.Properties.of() : BlockBehaviour.Properties.copy(builder.blockBuildRef);
            builder.block = () -> builder.blockFunc.apply(builder.blockBuild);
        }
        RegistryObject<Block> toReturn = this.blockRegisters.get(slot).register(id, builder.block);

        if (builder.itemBuild == null) builder.itemBuild = new Item.Properties();
        if (builder.attribute == null) {
            if (builder.itemFunc == null) builder.itemFunc = BlockItem::new;
        } else {
            if (builder.itemFunc == null)
                builder.itemFunc = (block, itemBuild) -> new AttributedBlockItem(block, itemBuild, builder.attribute);
        }
        this.itemRegisters.register(id, () -> builder.itemFunc.apply(toReturn.get(), builder.itemBuild), slot);

        return toReturn;
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block, Item.Properties itemBuild) {
        return this.register(id, block, itemBuild, 0);
    }

    public RegistryObject<Block> register(String id, Supplier<Block> block, Item.Properties itemBuild, int slot) {
        RegistryObject<Block> toReturn = this.blockRegisters.get(slot).register(id, block);
        this.itemRegisters.register(id, () -> new BlockItem(toReturn.get(), itemBuild), slot);
        return toReturn;
    }

    //  Eventbus register
    public void register(IEventBus eventBus) {
        this.blockRegisters.forEach(deferredRegister -> deferredRegister.register(eventBus));
        this.itemRegisters.register(eventBus);
    }

    //  Getter's and setter's
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

    public static class Builder {
        private Supplier<Block> block;
        private Function<BlockBehaviour.Properties, Block> blockFunc;
        private BiFunction<Block, Item.Properties, BlockItem> itemFunc;
        private BlockBehaviour.Properties blockBuild;
        private Block blockBuildRef;
        private Item.Properties itemBuild;
        private AttributedItem.Builder attribute;

        public Builder block(Supplier<Block> block) {
            this.block = block;
            return this;
        }

        public Builder blockFunc(Function<BlockBehaviour.Properties, Block> blockFunc) {
            this.blockFunc = blockFunc;
            return this;
        }

        public Builder itemFunc(BiFunction<Block, Item.Properties, BlockItem> itemFunc) {
            this.itemFunc = itemFunc;
            return this;
        }

        public Builder blockBuild(BlockBehaviour.Properties blockBuild) {
            this.blockBuild = blockBuild;
            return this;
        }

        public Builder blockBuildRef(Block blockBuildRef) {
            this.blockBuildRef = blockBuildRef;
            return this;
        }

        public Builder itemBuild(Item.Properties itemBuild) {
            this.itemBuild = itemBuild;
            return this;
        }

        public Builder attribute(AttributedItem.Builder attribute) {
            this.attribute = attribute;
            return this;
        }
    }
}