package dev.ftb.mods.ftbarmory.integration.productivebees;

import cy.jdkdigital.productivebees.common.block.entity.AdvancedBeehiveBlockEntity;
import cy.jdkdigital.productivelib.common.block.entity.IUpgradeableBlockEntity;
import cy.jdkdigital.productivelib.event.CollectValidUpgradesEvent;
import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ProductiveBeesCompat {
    private static final List<DeferredItem<ProductivityUpgradeItem>> UPGRADES = new ArrayList<>();

    private ProductiveBeesCompat() {}

    public static void init() {
        register(Metal.ADAMANTITE, 3.2);
        register(Metal.AETERNIUM, 3.8);
        register(Metal.AURICHALCUM, 4.5);
        NeoForge.EVENT_BUS.addListener(ProductiveBeesCompat::collectValidUpgrades);
    }

    private static void register(Metal metal, double productivity) {
        UPGRADES.add(Compat.add(ModItems.ITEMS.registerItem(
                metal.id() + "_productivity_upgrade",
                props -> new ProductivityUpgradeItem(props, productivity),
                props -> props.fireResistant())));
    }

    private static void collectValidUpgrades(CollectValidUpgradesEvent event) {
        if (event.getBlockEntity() instanceof AdvancedBeehiveBlockEntity) {
            UPGRADES.forEach(upgrade -> event.addValidUpgrade(upgrade.get()));
        }
    }

    public static double bonusProductivity(BlockEntity blockEntity) {
        if (!(blockEntity instanceof IUpgradeableBlockEntity upgradeable)) {
            return 0.0;
        }
        double bonus = 0.0;
        for (DeferredItem<ProductivityUpgradeItem> upgrade : UPGRADES) {
            bonus += upgradeable.getUpgradeCount(upgrade.get()) * upgrade.get().productivity;
        }
        return bonus;
    }
}
