package dev.ftb.mods.ftbarmory.events;

import dev.ftb.mods.ftbarmory.FTBArmory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = FTBArmory.MOD_ID)
public final class HarvestEvents {
    private HarvestEvents() {}

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        BlockState state = event.getTargetBlock();
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
