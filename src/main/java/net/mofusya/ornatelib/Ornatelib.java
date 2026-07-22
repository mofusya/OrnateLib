package net.mofusya.ornatelib;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.mofusya.ornatelib.lang.UnLong;
import org.slf4j.Logger;

@Mod(Ornatelib.MOD_ID)
public class Ornatelib {
    public static final String MOD_ID = "ornatelib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Ornatelib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        UnLong unLong = new UnLong(10, 60);

        LOGGER.info(unLong.toString());
        LOGGER.info(unLong.multi(new UnLong(0)).toString());

        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }
}