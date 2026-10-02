package dev.ftb.mods.ftbarmory.integration.ftbic;

import dev.ftb.mods.ftbarmory.common.OreMiningRules;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class QuarryMiningContext {
    private static final ThreadLocal<Permission> ACTIVE = new ThreadLocal<>();

    private QuarryMiningContext() {}

    public static boolean canUseTool(BlockState state, ItemStack tool) {
        return !tool.isEmpty() && tool.is(ItemTags.PICKAXES) && OreMiningRules.hasMiningTool(state, tool);
    }

    public static boolean allows(Player player, BlockState state, BlockPos pos) {
        Permission permission = ACTIVE.get();
        return permission != null
                && permission.player() == player
                && permission.state() == state
                && permission.pos().equals(pos)
                && canUseTool(state, permission.tool());
    }

    public static <T> T withTool(Player player, BlockState state, BlockPos pos, ItemStack tool, Supplier<T> operation) {
        Permission previous = ACTIVE.get();
        ACTIVE.set(new Permission(player, state, pos.immutable(), tool));
        try {
            return operation.get();
        } finally {
            if (previous == null) {
                ACTIVE.remove();
            } else {
                ACTIVE.set(previous);
            }
        }
    }

    private record Permission(Player player, BlockState state, BlockPos pos, ItemStack tool) {}
}
