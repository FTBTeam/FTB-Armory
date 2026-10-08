package dev.ftb.mods.ftbarmory.integration.enderio;

import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.world.item.Item;

public final class EnderIOCompat {
    private EnderIOCompat() {}

    public static void init() {
        for (Metal metal : Metal.values()) {
            Compat.add(ModItems.ITEMS.registerItem(
                    metal.id() + "_grinding_ball", Item::new, Item.Properties::fireResistant));
        }
    }
}
