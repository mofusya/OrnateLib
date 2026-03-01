package net.mofusya.ornatelib.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescriptionItem extends Item {
    public DescriptionItem(Properties build) {
        super(build);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (itemId == null) return;

        component.add(Component.translatable("item." + itemId.getNamespace() + "." + itemId.getPath() + ".desc").withStyle(ChatFormatting.DARK_GRAY));
    }
}
