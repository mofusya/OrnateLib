package net.mofusya.ornatelib.test;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.Ornatelib;
import net.mofusya.ornatelib.item.AttributedItem;
import net.mofusya.ornatelib.registries.OrnateItemDeferredRegister;
import net.mofusya.ornatelib.registries.toolset.ToolSet;

public class ModItems {
    public static final OrnateItemDeferredRegister ITEMS = OrnateItemDeferredRegister.create(Ornatelib.MOD_ID);

    public static final RegistryObject<Item> ITEM = ITEMS.register("test_item", Item::new, new Item.Properties().rarity(Rarity.EPIC));
    public static final ToolSet TEST = ITEMS.register("tool_set_test",  new ToolSet.Builder(Items.IRON_INGOT).property(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ATTRIBUTED_ITEM = ITEMS.register("attributed_item", new AttributedItem.Builder()
            .attribute("density", 7.874f, true)
            .attribute("melting_point", 1811, true)
            .attribute("boiling_point", 3134, true)
    );
}
