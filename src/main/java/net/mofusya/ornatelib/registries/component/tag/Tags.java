package net.mofusya.ornatelib.registries.component.tag;

public class Tags {

    public String MOD_ID;

    public final String ID;

    public Tags(String id) {
        ID = id;
    }

    public <T extends Tags> T setMod(String modId, T instance) {
        MOD_ID = modId;
        return instance;
    }
}
