package dev.ftb.mods.ftbarmory.common;

import dev.ftb.mods.ftbarmory.integration.apotheosis.OmneticHarvest;
import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryMiningContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;

public final class OreMiningRules {
    private static final boolean APOTHEOSIS = ModList.get().isLoaded("apotheosis");

    private OreMiningRules() {}

    public static boolean hasMiningTool(BlockState state, ItemInstance stack) {
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool != null && tool.isCorrectForDrops(state)) {
            return true;
        }
        return APOTHEOSIS && stack instanceof ItemStack itemStack && OmneticHarvest.allows(itemStack, state);
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
