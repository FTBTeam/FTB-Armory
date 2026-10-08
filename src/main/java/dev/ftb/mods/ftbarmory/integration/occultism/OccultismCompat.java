package dev.ftb.mods.ftbarmory.integration.occultism;

import com.klikli_dev.occultism.registry.OccultismDataComponents;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.world.item.Item;

public final class OccultismCompat {
    private OccultismCompat() {}

    public static void init() {
        Compat.add(ModItems.ITEMS.registerItem(
                "primordial_miner",
                Item::new,
                props -> props.fireResistant()
                        .durability(16383)
                        .component(OccultismDataComponents.MAX_MINING_TIME.get(), 10)
                        .component(OccultismDataComponents.ROLLS_PER_OPERATION.get(), 2)
                        .component(OccultismDataComponents.MINER_OUTPUT_MULTIPLIER.get(), 9)));
    }
}
