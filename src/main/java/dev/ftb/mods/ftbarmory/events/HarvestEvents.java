package dev.ftb.mods.ftbarmory.events;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.OreMiningRules;
import dev.ftb.mods.ftbarmory.registry.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@EventBusSubscriber(modid = FTBArmory.MOD_ID)
public final class HarvestEvents {
    private HarvestEvents() {}

    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        if (event.getState().is(ModTags.PROTECTED_ORES)
                && !OreMiningRules.canMine(event.getPlayer(), event.getState(), event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        BlockState state = event.getTargetBlock();
        if (state.is(ModTags.PROTECTED_ORES)
                && (event.getEntity().isFakePlayer()
                        || !OreMiningRules.hasMiningTool(
                                state, event.getEntity().getMainHandItem()))) {
            event.setCanHarvest(false);
            return;
        }
        if (!event.canHarvest() || !state.requiresCorrectToolForDrops()) {
            return;
        }
        if (!BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals(FTBArmory.MOD_ID)) {
            return;
        }
        Tool tool = event.getEntity().getMainHandItem().get(DataComponents.TOOL);
        if (tool != null && !tool.isCorrectForDrops(state)) {
            event.setCanHarvest(false);
        }
    }
}
