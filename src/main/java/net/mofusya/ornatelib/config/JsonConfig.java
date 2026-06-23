package net.mofusya.ornatelib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;
import org.openjdk.nashorn.internal.scripts.JO;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class JsonConfig {
    private final Gson gson;
    private final Path path;
    private JsonObject config;

    public JsonConfig(String fileName) {
        this(fileName, JsonObject::new);
    }

    public JsonConfig(String fileName, Supplier<JsonObject> config) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.path = FMLPaths.CONFIGDIR.get().resolve(fileName + ".json");
        this.config = config.get();
    }

    public JsonObject get(){
        return this.config;
    }

    public void load() {
        try {
            if (!Files.exists(this.path)) {
                this.save();
                return;
            }

            try (Reader reader = Files.newBufferedReader(this.path)) {
                this.config = this.gson.fromJson(reader, JsonObject.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.createDirectories(this.path.getParent());

            try (Writer writer = Files.newBufferedWriter(this.path)) {
                this.gson.toJson(this.config, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
