package net.mofusya.ornatelib.registries;

import com.google.common.base.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrnateItemDeferredRegister {

    private final ArrayList<DeferredRegister<Item>> itemRegisters = new ArrayList<>();

    private OrnateItemDeferredRegister(String modId, int itemRegisterSlotCount) {
        for (int i = 0; i < itemRegisterSlotCount; i++) {
            this.itemRegisters.add(DeferredRegister.create(ForgeRegistries.ITEMS, modId));
        }
    }

    public RegistryObject<Item> register(String id) {
        return this.register(id, Item::new, 0);
    }

    public RegistryObject<Item> register(String id, Item item) {
        return this.register(id, item, 0);
    }

    public RegistryObject<Item> register(String id, Item item, int slot) {
        return this.register(id, () -> item, slot);
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

    public void register(IEventBus eventBus) {
        this.itemRegisters.forEach(deferredRegister -> deferredRegister.register(eventBus));
    }

    public DeferredRegister<Item> getItemRegister() {
        return this.getItemRegister(0);
    }

    public DeferredRegister<Item> getItemRegister(int slot) {
        return this.itemRegisters.get(slot);
    }

    public List<RegistryObject<Item>> getMainItems(){
        return this.getItems(0);
    }

    public List<RegistryObject<Item>> getItems(){
        List<RegistryObject<Item>> toReturn = new ArrayList<>();
        for (DeferredRegister<Item> register : this.itemRegisters){
            toReturn.addAll(register.getEntries());
        }
        return toReturn;
    }

    public List<RegistryObject<Item>> getItems(int slot){
        return new ArrayList<>(this.getItemRegister(slot).getEntries());
    }

    public List<RegistryObject<Item>> getItems(int... slots){
        List<RegistryObject<Item>> toReturn = new ArrayList<>();
        for (int slot : slots){
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
