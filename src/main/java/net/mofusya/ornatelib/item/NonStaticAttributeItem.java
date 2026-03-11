package net.mofusya.ornatelib.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class NonStaticAttributeItem extends AttributedItem {
    public NonStaticAttributeItem(Properties build, Builder builder) {
        super(build, builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        String attribute;
        String value;
        for (Iterator<String> var5 = this.display.iterator(); var5.hasNext(); component.add(Component.translatable("item." + this.getModId() + ".attributed_item." + attribute).append(": ").append(Component.literal(value).withStyle(ChatFormatting.DARK_GRAY)))) {
            attribute = var5.next();
            value = "";
            if (this.integerAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getIntegerAttribute(itemStack, attribute));
            } else if (this.doubleAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getDoubleAttribute(itemStack, attribute));
            } else if (this.floatAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getFloatAttribute(itemStack, attribute));
            } else if (this.booleanAttribute.containsKey(attribute)) {
                value = String.valueOf(this.getBooleanAttribute(itemStack, attribute));
            } else if (this.stringAttribute.containsKey(attribute)) {
                value = this.getStringAttribute(itemStack, attribute);
            }
        }
    }

    public int getIntegerAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).getInt(attribute) : super.getIntegerAttribute(attribute);
    }

    public double getDoubleAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).getDouble(attribute) : super.getDoubleAttribute(attribute);
    }

    public float getFloatAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).getFloat(attribute) : super.getFloatAttribute(attribute);
    }

    public boolean getBooleanAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).getBoolean(attribute) : super.getBooleanAttribute(attribute);
    }

    public String getStringAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).getString(attribute) : super.getStringAttribute(attribute);
    }

    public Tag getStrangeAttribute(ItemStack itemStack, String attribute) {
        return itemStack.getOrCreateTag().getCompound(this.getModId()).contains(attribute) ? itemStack.getOrCreateTag().getCompound(this.getModId()).get(attribute) : super.getStrangeAttribute(attribute) instanceof Tag tag ? tag : itemStack.getOrCreateTag().getCompound(this.getModId()).get(attribute);
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, int value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.integerAttribute.containsKey(attribute) && item.integerAttribute.get(attribute) != value) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.putInt(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, double value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.doubleAttribute.containsKey(attribute) && item.doubleAttribute.get(attribute) != value) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.putDouble(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, float value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.floatAttribute.containsKey(attribute) && item.floatAttribute.get(attribute) != value) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.putFloat(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, boolean value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.booleanAttribute.containsKey(attribute) && item.booleanAttribute.get(attribute) != value) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.putBoolean(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, String value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.stringAttribute.containsKey(attribute) && !item.stringAttribute.get(attribute).equals(value)) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.putString(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }

    public ItemStack setAttribute(ItemStack itemStack, String attribute, Tag value) {
        if (itemStack.getItem() instanceof NonStaticAttributeItem item && item.strangeAttribute.containsKey(attribute) && item.strangeAttribute.get(attribute) != value) {
            CompoundTag tag = itemStack.getOrCreateTag().getCompound(this.getModId());
            tag.put(attribute, value);
            itemStack.getOrCreateTag().put(this.getModId(), tag);
        }
        return itemStack;
    }
}