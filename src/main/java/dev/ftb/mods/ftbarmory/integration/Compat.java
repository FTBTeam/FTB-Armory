package dev.ftb.mods.ftbarmory.integration;

import dev.ftb.mods.ftbarmory.integration.ae2.AE2Compat;
import dev.ftb.mods.ftbarmory.integration.agritech.AgritechCompat;
import dev.ftb.mods.ftbarmory.integration.apotheosis.ApotheosisCompat;
import dev.ftb.mods.ftbarmory.integration.artifice.ArtificeCompat;
import dev.ftb.mods.ftbarmory.integration.enderio.EnderIOCompat;
import dev.ftb.mods.ftbarmory.integration.ftbic.FtbicOverclockers;
import dev.ftb.mods.ftbarmory.integration.justdirefuels.JustDireFuelsCompat;
import dev.ftb.mods.ftbarmory.integration.neovitae.NeoVitaeCompat;
import dev.ftb.mods.ftbarmory.integration.occultism.OccultismCompat;
import dev.ftb.mods.ftbarmory.integration.oritech.OritechCompat;
import dev.ftb.mods.ftbarmory.integration.productivebees.ProductiveBeesCompat;
import dev.ftb.mods.ftbarmory.integration.sophisticated.SophisticatedBackpacksCompat;
import dev.ftb.mods.ftbarmory.integration.sophisticated.SophisticatedStorageCompat;
import dev.ftb.mods.ftbarmory.integration.thaumaturge.ThaumaturgeCompat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;

public final class Compat {
    public static final List<DeferredItem<? extends Item>> ITEMS = new ArrayList<>();

    private Compat() {}

    public static boolean loaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static <T extends Item> DeferredItem<T> add(DeferredItem<T> item) {
        ITEMS.add(item);
        return item;
    }

    public static void init(IEventBus eventBus, Dist dist) {
        if (loaded("enderio")) {
            EnderIOCompat.init();
        }
        if (loaded("occultism")) {
            OccultismCompat.init();
        }
        if (loaded("neovitae")) {
            NeoVitaeCompat.init();
        }
        if (loaded("ae2")) {
            AE2Compat.init();
        }
        if (loaded("sophisticatedstorage")) {
            SophisticatedStorageCompat.init();
        }
        if (loaded("sophisticatedbackpacks")) {
            SophisticatedBackpacksCompat.init();
        }
        if (loaded("irons_artifice")) {
            ArtificeCompat.init();
        }
        if (loaded("oritech")) {
            OritechCompat.init(eventBus);
        }
        if (loaded("ftbic")) {
            FtbicOverclockers.init();
        }
        if (loaded("productivebees")) {
            ProductiveBeesCompat.init();
        }
        if (loaded("justdirefuels")) {
            JustDireFuelsCompat.init();
        }
        if (loaded("agritechevolved")) {
            AgritechCompat.init();
        }
        if (loaded("thaumaturge")) {
            ThaumaturgeCompat.init(eventBus);
        }
        if (loaded("apotheosis")) {
            ApotheosisCompat.init();
        }
    }
}
