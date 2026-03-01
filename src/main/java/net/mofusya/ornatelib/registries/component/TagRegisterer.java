package net.mofusya.ornatelib.registries.component;

import net.mofusya.ornatelib.registries.component.tag.Tags;

public class TagRegisterer {
    public final String MOD_ID;

    public TagRegisterer(String modId) {
        MOD_ID = modId;
    }

    public <T extends Tags> T register(T instance) {
        return instance.setMod(MOD_ID, instance);
    }
}