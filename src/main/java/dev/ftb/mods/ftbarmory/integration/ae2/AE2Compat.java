package dev.ftb.mods.ftbarmory.integration.ae2;

import appeng.api.stacks.AEKeyType;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import appeng.items.materials.StorageComponentItem;
import appeng.items.storage.BasicStorageCell;
import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public final class AE2Compat {
    private record Tier(Metal metal, int kilobytes, double idleDrain) {}

    private static final List<Tier> TIERS = List.of(
            new Tier(Metal.ADAMANTITE, 1024, 3.0),
            new Tier(Metal.AETERNIUM, 4096, 3.5),
            new Tier(Metal.AURICHALCUM, 16384, 4.0));

    private static final List<DeferredItem<BasicStorageCell>> CELLS = new ArrayList<>();

    private AE2Compat() {}

    public static void init(IEventBus eventBus) {
        for (Tier tier : TIERS) {
            String name = tier.metal().id();
            Compat.add(ModItems.ITEMS.registerItem(
                    name + "_cell_component",
                    props -> new StorageComponentItem(props, tier.kilobytes()),
                    Item.Properties::fireResistant));
            CELLS.add(Compat.add(ModItems.ITEMS.registerItem(
                    name + "_item_storage_cell",
                    props -> new BasicStorageCell(
                            props.stacksTo(1),
                            tier.idleDrain(),
                            tier.kilobytes(),
                            tier.kilobytes() * 8,
                            63,
                            AEKeyType.items()),
                    Item.Properties::fireResistant)));
        }
        eventBus.addListener(AE2Compat::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            String group = GuiText.StorageCells.getTranslationKey();
            for (DeferredItem<BasicStorageCell> cell : CELLS) {
                Upgrades.add(AEItems.FUZZY_CARD, cell, 1, group);
                Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group);
                Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group);
                Upgrades.add(AEItems.VOID_CARD, cell, 1, group);
            }
        });
    }
}
