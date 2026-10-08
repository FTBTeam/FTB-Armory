package dev.ftb.mods.ftbarmory.integration.justdirefuels;

import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.world.item.Item;

public final class JustDireFuelsCompat {
    private JustDireFuelsCompat() {}

    public static void init() {
        Compat.add(ModItems.ITEMS.registerItem("aurichalcum_ember", Item::new, Item.Properties::fireResistant));
    }
}
