package net.mofusya.ornatelib.registries;

import net.minecraft.resources.ResourceLocation;
import net.mofusya.ornatelib.util.ArrayMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrnateRegistry<T> {
    protected final Map<ResourceLocation, OrnateHolder<T>> R;

    public OrnateRegistry() {
        this.R = new HashMap<>();
    }

    public Map<ResourceLocation, OrnateHolder<T>> getEntries(){
        return Map.copyOf(this.R);
    }

    public void register(OrnateRegister<T> register){
        register.getEntries().forEach(R::putAll);
    }
}
