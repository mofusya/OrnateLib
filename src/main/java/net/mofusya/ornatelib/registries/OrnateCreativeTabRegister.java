package net.mofusya.ornatelib.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OrnateCreativeTabRegister {
    private final ArrayList<DeferredRegister<CreativeModeTab>> registers = new ArrayList<>();
    private final String modId;

    public OrnateCreativeTabRegister(String modId) {
        this(modId, 1);
    }

    public OrnateCreativeTabRegister(String modId, int indexCount) {
        this.modId = modId;
        for (int i = 0; i < indexCount; i++) {
            this.registers.add(DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId));
        }
    }

    //  Registering.
    public RegistryObject<CreativeModeTab> register(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
        return this.register(id, icon, displayItems, 0);
    }

    public RegistryObject<CreativeModeTab> register(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems, int index) {
        return this.register(id, builder -> builder.title(this.createTitle(id)).icon(icon).displayItems(displayItems).build(), index);
    }

    public RegistryObject<CreativeModeTab> register(String id, Component title, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
        return this.register(id, title, icon, displayItems, 0);
    }

    public RegistryObject<CreativeModeTab> register(String id, Component title, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems, int index) {
        return this.register(id, builder -> builder.title(title).icon(icon).displayItems(displayItems).build(), index);
    }

    public RegistryObject<CreativeModeTab> register(String id, Function<CreativeModeTab.Builder, CreativeModeTab> creativeTabFunc) {
        return this.register(id, creativeTabFunc, 0);
    }

    public RegistryObject<CreativeModeTab> register(String id, Function<CreativeModeTab.Builder, CreativeModeTab> creativeTabFunc, int index) {
        return this.register(id, () -> creativeTabFunc.apply(CreativeModeTab.builder()), index);
    }

    public RegistryObject<CreativeModeTab> register(String id, Supplier<CreativeModeTab> creativeTab) {
        return this.register(id, creativeTab, 0);
    }

    public RegistryObject<CreativeModeTab> register(String id, Supplier<CreativeModeTab> creativeTab, int index) {
        return this.registers.get(index).register(id, creativeTab);
    }

    //  Eventbus register.
    public void register(IEventBus eventBus) {
        this.registers.forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    //  Getter and Setters.
    public DeferredRegister<CreativeModeTab> getRegister() {
        return this.getRegister(0);
    }

    public DeferredRegister<CreativeModeTab> getRegister(int index) {
        return this.registers.get(index);
    }

    public ArrayList<DeferredRegister<CreativeModeTab>> getRegisters() {
        return new ArrayList<>(this.registers);
    }

    public List<RegistryObject<CreativeModeTab>> getMainCreativeTabs() {
        return this.getCreativeTabs(0);
    }

    public List<RegistryObject<CreativeModeTab>> getCreativeTabs() {
        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i < this.registers.size(); i++) {
            allIndexes.add(i);
        }
        return this.getCreativeTabs(allIndexes.toArray(Integer[]::new));
    }

    public List<RegistryObject<CreativeModeTab>> getCreativeTabs(Integer index) {
        return new ArrayList<>(this.getRegister(index).getEntries());
    }

    public List<RegistryObject<CreativeModeTab>> getCreativeTabs(Integer... indexes) {
        List<RegistryObject<CreativeModeTab>> blockEntityTypes = new ArrayList<>();
        for (int index : indexes) {
            blockEntityTypes.addAll(this.getRegister(index).getEntries());
        }
        return blockEntityTypes;
    }

    public Component createTitle(String id){
        return Component.translatable("creativetab." + this.modId + "." + id);
    }
}
