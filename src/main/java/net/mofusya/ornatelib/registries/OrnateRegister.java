package net.mofusya.ornatelib.registries;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.mofusya.ornatelib.Ornatelib;
import net.mofusya.ornatelib.util.annotation.EverythingAreNonNullByDefault;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@EverythingAreNonNullByDefault
public class OrnateRegister<T> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final String modId;
    private final List<Map<ResourceLocation, OrnateHolder<T>>> values;
    private final RegistryConsumer<T> registry;
    private boolean locked = false;

    public OrnateRegister(RegistryConsumer<T> registry) {
        this(registry, 1);
    }

    public OrnateRegister(RegistryConsumer<T> registry, @Range(from = 1, to = Integer.MAX_VALUE) int slotCount) {
        this(null, registry, slotCount);
    }

    public OrnateRegister(@Nullable String modId, RegistryConsumer<T> registry) {
        this(modId, registry, 1);
    }

    public OrnateRegister(@Nullable String modId, RegistryConsumer<T> registry, @Range(from = 1, to = Integer.MAX_VALUE) int slotCount) {
        this.modId = modId == null ? Ornatelib.MOD_ID : modId;
        this.values = new ArrayList<>();
        for (int i = 0; i < slotCount; i++) {
            values.add(new HashMap<>());
        }
        this.registry = registry;
    }

    public OrnateHolder<T> register(String id, Supplier<T> value) {
        return register(id, value, 0);
    }

    public OrnateHolder<T> register(String id, Supplier<T> value, @Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        return register(new ResourceLocation(this.getModId(), id), value, slot);
    }

    public OrnateHolder<T> register(ResourceLocation id, Supplier<T> value) {
        return register(id, value, 0);
    }

    public OrnateHolder<T> register(ResourceLocation id, Supplier<T> value, @Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        if (this.isLocked()) throw new RuntimeException("The registry is locked.");

        var holder = new OrnateHolder<>(id, value);
        this.values.get(slot).put(id, holder);
        return holder;
    }

    public void register() {
        this.lock();
        this.values.forEach(this.getRegistry()::register);
    }

    public String getModId() {
        return modId;
    }

    public Map<ResourceLocation, OrnateHolder<T>> getEntries(int slot) {
        return Map.copyOf(this.getEntries().get(slot));
    }

    public List<Map<ResourceLocation, OrnateHolder<T>>> getEntries() {
        return new ArrayList<>(this.values);
    }

    private RegistryConsumer<T> getRegistry() {
        return registry;
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        this.locked = true;
    }

    @FunctionalInterface
    public interface RegistryConsumer<T> {
        void register(Map<ResourceLocation, OrnateHolder<T>> registry);
    }
}
