package net.mofusya.ornatelib.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OrnateBlockEntityTypeRegister {

    private final ArrayList<DeferredRegister<BlockEntityType<?>>> registers = new ArrayList<>();

    public OrnateBlockEntityTypeRegister(String modId) {
        this(modId, 1);
    }

    public OrnateBlockEntityTypeRegister(String modId, int indexCount) {
        for (int i = 0; i < indexCount; i++) {
            this.registers.add(DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, modId));
        }
    }

    //  Registering.
    public <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return this.registers.get(0).register(id, blockEntityType);
    }

    public <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> blockEntityType, int index) {
        return this.registers.get(index).register(id, blockEntityType);
    }

    //  Eventbus register.
    public void register(IEventBus eventBus) {
        this.registers.forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    //  Getter and Setters.
    public DeferredRegister<BlockEntityType<?>> getRegister() {
        return this.getRegister(0);
    }

    public DeferredRegister<BlockEntityType<?>> getRegister(int index) {
        return this.registers.get(index);
    }

    public ArrayList<DeferredRegister<BlockEntityType<?>>> getRegisters() {
        return new ArrayList<>(this.registers);
    }

    public List<RegistryObject<BlockEntityType<?>>> getMainBlockEntityTypes() {
        return this.getBlockEntityTypes(0);
    }

    public List<RegistryObject<BlockEntityType<?>>> getBlockEntityTypes() {
        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i < this.registers.size(); i++) {
            allIndexes.add(i);
        }
        return this.getBlockEntityTypes(allIndexes.toArray(Integer[]::new));
    }

    public List<RegistryObject<BlockEntityType<?>>> getBlockEntityTypes(Integer index) {
        return new ArrayList<>(this.getRegister(index).getEntries());
    }

    public List<RegistryObject<BlockEntityType<?>>> getBlockEntityTypes(Integer... indexes) {
        List<RegistryObject<BlockEntityType<?>>> blockEntityTypes = new ArrayList<>();
        for (int index : indexes) {
            blockEntityTypes.addAll(this.getRegister(index).getEntries());
        }
        return blockEntityTypes;
    }
}
