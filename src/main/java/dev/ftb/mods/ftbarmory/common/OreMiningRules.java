package dev.ftb.mods.ftbarmory.common;

import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryMiningContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;

public final class OreMiningRules {
    private OreMiningRules() {}

    public static boolean hasMiningTool(BlockState state, ItemInstance stack) {
        Tool tool = stack.get(DataComponents.TOOL);
        return tool != null && tool.isCorrectForDrops(state);
    }

    public static boolean canMine(Player player, BlockState state, BlockPos pos) {
        if (QuarryMiningContext.allows(player, state, pos)) {
            return true;
        }
        if (player.isFakePlayer() || player.isSpectator()) {
            return false;
        }
        // Match the reach tolerance used by the server's normal block-breaking check.
        return player.isWithinBlockInteractionRange(pos, 1.0)
                && (player.isCreative() || hasMiningTool(state, player.getMainHandItem()));
    }
}
