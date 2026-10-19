package dev.ftb.mods.ftbarmory.integration.ironfurnaces;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public final class IronFurnacesCompat {
    private static final ResourceKey<CreativeModeTab> TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("ironfurnaces_tab"));
    private static final List<String> FURNACES =
            List.of("netherite_furnace", "allthemodium_furnace", "vibranium_furnace", "unobtainium_furnace");
    private static final List<String> UPGRADES =
            List.of("upgrade_netherite", "upgrade_allthemodium", "upgrade_vibranium", "upgrade_unobtainium");

    private IronFurnacesCompat() {}

    public static void init(IEventBus eventBus) {
        eventBus.addListener(IronFurnacesCompat::onBuildCreativeTabs);
    }

    private static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TAB) {
            insertChain(event, FURNACES);
            insertChain(event, UPGRADES);
        }
    }

    private static void insertChain(BuildCreativeModeTabContentsEvent event, List<String> chain) {
        ItemStack previous = stack(chain.getFirst());
        for (String path : chain.subList(1, chain.size())) {
            ItemStack next = stack(path);
            if (!event.getParentEntries().contains(next)) {
                event.insertAfter(previous, next, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            previous = next;
        }
    }

    private static ItemStack stack(String path) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(id(path)));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("ironfurnaces", path);
    }
}
