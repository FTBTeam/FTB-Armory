package dev.ftb.mods.ftbarmory.integration.apotheosis;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import dev.shadowsoffire.apotheosis.affix.salvaging.SalvageItem;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import net.minecraft.world.item.Rarity;

public final class ApotheosisCompat {
    private ApotheosisCompat() {}

    public static void init() {
        Compat.add(ModItems.ITEMS.registerItem(
                "aurichalcum_relic",
                props -> new SalvageItem(RarityRegistry.INSTANCE.holder(FTBArmory.id("exalted")), props),
                props -> props.fireResistant().rarity(Rarity.EPIC)));
    }
}
