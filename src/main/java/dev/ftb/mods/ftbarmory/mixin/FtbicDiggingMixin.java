package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryMiningContext;
import dev.ftb.mods.ftbarmory.integration.ftbic.QuarryToolAccess;
import dev.ftb.mods.ftbarmory.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbic.block.entity.machine.DiggingBaseBlockEntity", remap = false)
public abstract class FtbicDiggingMixin {
    @Inject(method = "isValidBlock", at = @At("HEAD"), cancellable = true)
    private void ftbarmory$allowEquippedQuarry(BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(ModTags.PROTECTED_ORES) && this instanceof QuarryToolAccess quarry) {
            // Override only the base relocation check; the quarry still applies its own filter.
            cir.setReturnValue(QuarryMiningContext.canUseTool(state, quarry.ftbarmory$getQuarryTool()));
        }
    }

    @Redirect(
            method = "canBreak",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/neoforged/neoforge/common/CommonHooks;fireBlockBreak(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/GameType;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/neoforged/neoforge/event/level/block/BreakBlockEvent;"))
    private BreakBlockEvent ftbarmory$checkQuarryPermission(
            Level level, GameType mode, Player player, BlockPos pos, BlockState state) {
        if (state.is(ModTags.PROTECTED_ORES) && this instanceof QuarryToolAccess quarry) {
            ItemStack tool = quarry.ftbarmory$getQuarryTool();
            return QuarryMiningContext.withTool(
                    player, state, pos, tool, () -> CommonHooks.fireBlockBreak(level, mode, player, pos, state));
        }
        return CommonHooks.fireBlockBreak(level, mode, player, pos, state);
    }
}
