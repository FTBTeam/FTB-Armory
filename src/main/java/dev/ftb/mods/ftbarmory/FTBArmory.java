package dev.ftb.mods.ftbarmory;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbarmory.registry.ModBlocks;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(FTBArmory.MOD_ID)
public class FTBArmory {
    public static final String MOD_ID = "ftbarmory";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public FTBArmory(IEventBus eventBus, ModContainer container, Dist dist) {
        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        eventBus.addListener(ModItems::onBuildCreativeTabs);
    }
}
