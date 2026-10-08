package dev.ftb.mods.ftbarmory.integration.agritech;

import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.world.item.Item;

public final class AgritechCompat {
    private AgritechCompat() {}

    public static void init() {
        for (Metal metal : Metal.values()) {
            Compat.add(
                    ModItems.ITEMS.registerItem(metal.id() + "_fertilizer", Item::new, Item.Properties::fireResistant));
        }
    }
}
