package net.mofusya.ornatelib.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.ornatelib.Ornatelib;
import net.mofusya.ornatelib.item.ShiftDescriptionItem;
import net.mofusya.ornatelib.items.item.SubItem;
import net.mofusya.ornatelib.registries.OrnateItemDeferredRegister;

public class ModItems {
    public static final OrnateItemDeferredRegister ITEMS = OrnateItemDeferredRegister.create(Ornatelib.MOD_ID, 2);

    public static final RegistryObject<Item> ITEM = ITEMS.register("test_item", ShiftDescriptionItem::new, new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> ITEM2 = ITEMS.register("test_item2", SubItem::new, 1);
}
