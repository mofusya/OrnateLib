package net.mofusya.ornatelib.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShiftDescriptionItem extends DescriptionItem{
    public ShiftDescriptionItem(Properties build) {
        super(build);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {

        if (Screen.hasShiftDown()){
            super.appendHoverText(itemStack, level, component, flag);
        } else {
            component.add(Component.translatable("text.ornatelib.press_shift_2_show_description"));
        }
    }
}
