package net.mofusya.ornatelib.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.Ornatelib;
import net.mofusya.ornatelib.registries.OrnateItemDeferredRegister;
import net.mofusya.ornatelib.registries.toolset.ToolSet;

public class ModItems {
    public static final OrnateItemDeferredRegister ITEMS = OrnateItemDeferredRegister.create(Ornatelib.MOD_ID);

    public static final RegistryObject<Item> ITEM = ITEMS.register("test_item", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final ToolSet TEST = ITEMS.register("tool_set_test", ToolSet.Builder.create(Items.IRON_INGOT).property(new Item.Properties().rarity(Rarity.EPIC)));
}
