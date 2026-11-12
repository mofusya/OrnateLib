package net.mofusya.ornatelib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import org.openjdk.nashorn.internal.scripts.JO;
import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class JsonConfig {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Gson gson;
    private final Path path;
    private JsonElement config;

    public JsonConfig(String fileName) {
        this(fileName, JsonObject::new);
    }

    public JsonConfig(String fileName, Supplier<JsonElement> config) {
        this(fileName, config.get());
    }

    public JsonConfig(String fileName, JsonElement config) {
        this(FMLPaths.CONFIGDIR.get().resolve(fileName + ".json"), config);
    }

    public JsonConfig(Path path) {
        this(path, JsonObject::new);
    }

    public JsonConfig(Path path, Supplier<JsonElement> config) {
        this(path, config.get());
    }

    public JsonConfig(Path path, JsonElement config){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.path = path;
        this.config = config;
    }

    public JsonObject get(){
        return this.getAs(JsonObject.class);
    }

    @SuppressWarnings("unchecked")
    public <JSON extends JsonElement> JSON getAs(Class<JSON> jsonClass){
        return (JSON) this.config;
    }

    public void load() {
        try {
            if (!Files.exists(this.path)) {
                this.save();
                return;
            }

            try (Reader reader = Files.newBufferedReader(this.path)) {
                this.config = this.gson.fromJson(reader, JsonElement.class);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load Json config, {}", this.path.toString(), e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(this.path.getParent());

            try (Writer writer = Files.newBufferedWriter(this.path)) {
                this.gson.toJson(this.config, writer);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save Json config, {}", this.path.toString(), e);
        }
    }
}
