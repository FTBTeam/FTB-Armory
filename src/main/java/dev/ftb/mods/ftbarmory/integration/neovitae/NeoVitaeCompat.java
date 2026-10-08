package dev.ftb.mods.ftbarmory.integration.neovitae;

import com.breakinblocks.neovitae.common.item.BloodOrbItem;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;

public final class NeoVitaeCompat {
    private NeoVitaeCompat() {}

    public static void init() {
        Compat.add(ModItems.ITEMS.registerItem(
                "aurichalcum_blood_orb",
                BloodOrbItem::new,
                props -> props.fireResistant().stacksTo(1)));
    }
}
