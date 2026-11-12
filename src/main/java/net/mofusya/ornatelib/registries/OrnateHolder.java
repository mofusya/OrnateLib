package net.mofusya.ornatelib.registries;

import net.minecraft.resources.ResourceLocation;
import net.mofusya.ornatelib.util.annotation.EverythingAreNonNullByDefault;

import java.util.function.Supplier;

@EverythingAreNonNullByDefault
public record OrnateHolder<T>(ResourceLocation key, Supplier<T> value) {
    public T get(){
        return this.value().get();
    }
}
