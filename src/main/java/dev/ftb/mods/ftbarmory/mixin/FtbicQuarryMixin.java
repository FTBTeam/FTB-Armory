package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryMiningContext;
import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryToolAccess;
import dev.ftb.mods.ftbarmory.registry.ModTags;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbic.block.entity.machine.QuarryBlockEntity", remap = false)
public abstract class FtbicQuarryMixin implements QuarryToolAccess {
    @Shadow
    public ItemStack pickaxeStack;

    @Override
    public ItemStack ftbarmory$getQuarryTool() {
        return pickaxeStack;
    }

    @Inject(method = "digBlock", at = @At("HEAD"), cancellable = true)
    private void ftbarmory$recheckQuarryTool(BlockState state, BlockPos pos, CallbackInfo ci) {
        // The slotted tool may have changed or broken since this target was selected.
        if (state.is(ModTags.PROTECTED_ORES) && !QuarryMiningContext.canUseTool(state, pickaxeStack)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "digBlock",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemInstance;)Ljava/util/List;"))
    private List<ItemStack> ftbarmory$harvestWithQuarryTool(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            BlockEntity blockEntity,
            Entity entity,
            ItemInstance tool) {
        if (state.is(ModTags.PROTECTED_ORES) && entity instanceof Player player && tool == pickaxeStack) {
            return QuarryMiningContext.withTool(
                    player,
                    state,
                    pos,
                    pickaxeStack,
                    () -> Block.getDrops(state, level, pos, blockEntity, entity, tool));
        }
        return Block.getDrops(state, level, pos, blockEntity, entity, tool);
    }
}
