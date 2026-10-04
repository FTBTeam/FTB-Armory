package dev.ftb.mods.ftbarmory.common.block;

import dev.ftb.mods.ftbarmory.common.OreMiningRules;
import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryMiningContext;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public final class ProtectedOreBlock extends Block {
    public ProtectedOreBlock(Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return entity instanceof Player player && OreMiningRules.canMine(player, state, pos);
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return OreMiningRules.canMine(player, state, pos)
                ? super.getDestroyProgress(state, player, level, pos)
                : Float.MIN_VALUE;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        Entity miner = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        ItemInstance tool = params.getOptionalParameter(LootContextParams.TOOL);
        Vec3 origin = params.getOptionalParameter(LootContextParams.ORIGIN);
        if (!(miner instanceof Player player)
                || tool == null
                || !OreMiningRules.hasMiningTool(state, tool)
                || origin == null) {
            return List.of();
        }
        BlockPos pos = BlockPos.containing(origin);
        if (!QuarryMiningContext.allows(player, state, pos)
                && (player.isFakePlayer() || player.isSpectator() || !player.isWithinBlockInteractionRange(pos, 1.0))) {
            return List.of();
        }
        return super.getDrops(state, params);
    }

    @Override
    protected void onExplosionHit(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            Explosion explosion,
            BiConsumer<ItemStack, BlockPos> onHit) {
        // Ores must remain available for manual mining after an explosion.
    }
}
