package net.mofusya.ornatelib.test;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.Ornatelib;
import net.mofusya.ornatelib.item.AttributedItem;
import net.mofusya.ornatelib.registries.OrnateBlockDeferredRegister;

public class ModBlocks {
    public static final OrnateBlockDeferredRegister BLOCKS = OrnateBlockDeferredRegister.create(Ornatelib.MOD_ID);

    public static final RegistryObject<Block> TEST_BLOCK = BLOCKS.register("test_block", new OrnateBlockDeferredRegister.Builder().attribute(new AttributedItem.Builder()
            .attribute("density", 7.874f, true)
            .attribute("melting_point", 1811, true)
            .attribute("boiling_point", 3134, true)
    ));
}
