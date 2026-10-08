package dev.ftb.mods.ftbarmory.integration.sophisticated;

import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeItem;

public final class SophisticatedBackpacksCompat {
    private SophisticatedBackpacksCompat() {}

    public static void init() {
        register(Metal.ADAMANTITE, 16.0);
        register(Metal.AETERNIUM, 24.0);
        register(Metal.AURICHALCUM, 32.0);
    }

    private static void register(Metal metal, double multiplier) {
        Compat.add(ModItems.ITEMS.registerItem(
                metal.id() + "_backpack_stack_upgrade",
                props -> new StackUpgradeItem(multiplier, Config.SERVER.maxUpgradesPerStorage, props),
                Item.Properties::fireResistant));
    }
}
