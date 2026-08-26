package net.mofusya.ornatelib.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mofusya.ornatelib.util.function.QuadFunction;
import org.jetbrains.annotations.Nullable;

public class SimpleRightClickItem extends Item {

    private final QuadFunction<SimpleRightClickItem, Level, Player, InteractionHand, @Nullable InteractionResultHolder<ItemStack>> useFunc;

    public SimpleRightClickItem(Properties build, QuadFunction<SimpleRightClickItem, Level, Player, InteractionHand, InteractionResultHolder<ItemStack>> useFunc) {
        super(build);
        this.useFunc = useFunc;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        @Nullable
        InteractionResultHolder<ItemStack> returnValue = this.useFunc.apply(this, level, player, hand);
        return returnValue == null ? super.use(level, player, hand) : returnValue;
    }
}
