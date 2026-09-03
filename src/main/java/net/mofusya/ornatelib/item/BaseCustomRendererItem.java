package net.mofusya.ornatelib.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public abstract class BaseCustomRendererItem extends Item {
    public BaseCustomRendererItem(Properties build) {
        super(build);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BaseCustomRendererItem.this.getClientRenderer();
            }
        });
    }

    public abstract BlockEntityWithoutLevelRenderer getClientRenderer();
}
