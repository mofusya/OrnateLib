package net.mofusya.ornatelib.registries.component.tag;

@Deprecated(since = "1.20.1-alpha0.9a-forge")
public class Tags {

    public String modId;

    public final String id;

    public Tags(String id) {
        this.id = id;
    }

    public <T extends Tags> T setMod(String modId, T instance) {
        this.modId = modId;
        return instance;
    }
}
