package net.mofusya.ornatelib.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributedItem extends Item {

    protected final Map<String, Integer> integerAttribute;
    protected final Map<String, Double> doubleAttribute;
    protected final Map<String, Float> floatAttribute;
    protected final Map<String, Boolean> booleanAttribute;
    protected final Map<String, String> stringAttribute;
    protected final Map<String, Object> strangeAttribute;
    protected final ArrayList<String> display;

    public AttributedItem(Properties build, Builder builder) {
        super(build);

        this.integerAttribute = new HashMap<>(builder.integerAttribute);
        this.doubleAttribute = new HashMap<>(builder.doubleAttribute);
        this.floatAttribute = new HashMap<>(builder.floatAttribute);
        this.booleanAttribute = new HashMap<>(builder.booleanAttribute);
        this.stringAttribute = new HashMap<>(builder.stringAttribute);
        this.strangeAttribute = new HashMap<>(builder.strangeAttribute);
        this.display = new ArrayList<>(builder.display);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, component, flag);
        for (String attribute : this.display) {

            String value = "";
            if (this.integerAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getIntegerAttribute(attribute));

            } else if (this.doubleAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getDoubleAttribute(attribute));

            } else if (this.floatAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getFloatAttribute(attribute));

            } else if (this.booleanAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getBooleanAttribute(attribute));

            } else if (this.stringAttribute.containsKey(attribute)) {
                value = this.getStringAttribute(attribute);

            }

            component.add(Component.translatable("item." + this.getModId() + ".attributed_item." + attribute).append(": ").append(Component.literal(value).withStyle(ChatFormatting.DARK_GRAY)));
        }
    }

    public static class Builder {
        public final Map<String, Integer> integerAttribute = new HashMap<>();
        public final Map<String, Double> doubleAttribute = new HashMap<>();
        public final Map<String, Float> floatAttribute = new HashMap<>();
        public final Map<String, Boolean> booleanAttribute = new HashMap<>();
        public final Map<String, String> stringAttribute = new HashMap<>();
        public final Map<String, Object> strangeAttribute = new HashMap<>();
        public final ArrayList<String> display = new ArrayList<>();

        public Builder attribute(String string, int value) {
            return this.attribute(string, value, false);
        }

        public Builder attribute(String string, int value, boolean display) {
            if (display) this.display.add(string);
            this.integerAttribute.put(string, value);
            return this;
        }

        public Builder attribute(String string, double value) {
            return this.attribute(string, value, false);
        }

        public Builder attribute(String string, double value, boolean display) {
            if (display) this.display.add(string);
            this.doubleAttribute.put(string, value);
            return this;
        }

        public Builder attribute(String string, float value) {
            return this.attribute(string, value, false);
        }

        public Builder attribute(String string, float value, boolean display) {
            if (display) this.display.add(string);
            this.floatAttribute.put(string, value);
            return this;
        }

        public Builder attribute(String string, boolean value) {
            return this.attribute(string, value, false);
        }

        public Builder attribute(String string, boolean value, boolean display) {
            if (display) this.display.add(string);
            this.booleanAttribute.put(string, value);
            return this;
        }

        public Builder attribute(String string, String value) {
            return this.attribute(string, value, false);
        }

        public Builder attribute(String string, String value, boolean display) {
            if (display) this.display.add(string);
            this.stringAttribute.put(string, value);
            return this;
        }

        public Builder strangeAttribute(String string, Object value) {
            this.strangeAttribute.put(string, value);
            return this;
        }

        public Builder copy() {
            Builder toReturn = new Builder();
            toReturn.integerAttribute.putAll(this.integerAttribute);
            toReturn.doubleAttribute.putAll(this.doubleAttribute);
            toReturn.floatAttribute.putAll(this.floatAttribute);
            toReturn.booleanAttribute.putAll(this.booleanAttribute);
            toReturn.stringAttribute.putAll(this.stringAttribute);
            toReturn.strangeAttribute.putAll(this.strangeAttribute);
            toReturn.display.addAll(this.display);
            return toReturn;
        }
    }

    public int getIntegerAttribute(String attribute) {
        return this.integerAttribute.get(attribute);
    }

    public double getDoubleAttribute(String attribute) {
        return this.doubleAttribute.get(attribute);
    }

    public float getFloatAttribute(String attribute) {
        return this.floatAttribute.get(attribute);
    }

    public boolean getBooleanAttribute(String attribute) {
        return this.booleanAttribute.get(attribute);
    }

    public String getStringAttribute(String attribute) {
        return this.stringAttribute.get(attribute);
    }

    public Object getStrangeAttribute(String attribute) {
        return this.strangeAttribute.get(attribute);
    }

    protected String getModId() {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
        if (id != null) {
            return id.getNamespace();
        }
        return null;
    }
}