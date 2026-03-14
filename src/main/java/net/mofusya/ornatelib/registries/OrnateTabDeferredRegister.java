package net.mofusya.ornatelib.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class OrnateTabDeferredRegister {
    private final DeferredRegister<CreativeModeTab> tabRegister;
    private final String modId;

    private OrnateTabDeferredRegister(String modId) {
        this.modId = modId;
        this.tabRegister = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
    }

    public RegistryObject<CreativeModeTab> register(ItemLike icon, Consumer<CreativeModeTab.Output> outputFunc) {
        return this.register("main", icon, outputFunc);
    }

    public RegistryObject<CreativeModeTab> register(String id, ItemLike icon, Consumer<CreativeModeTab.Output> outputFunc) {
        return this.tabRegister.register(id, () -> CreativeModeTab.builder().icon(() -> new ItemStack(icon))
                .title(Component.translatable("tab." + this.modId + "." + id))
                .displayItems((parameters, output) -> outputFunc.accept(output))
                .build());
    }

    public void register(IEventBus eventBus) {
        this.tabRegister.register(eventBus);
    }

    public static OrnateTabDeferredRegister create(String modId){
        return new OrnateTabDeferredRegister(modId);
    }
}
