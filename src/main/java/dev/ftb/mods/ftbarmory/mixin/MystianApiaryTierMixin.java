package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.productivebees.ProductiveBeesCompat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.mystian.apiary.integration.ProductiveLibUpgrades", remap = false)
public abstract class MystianApiaryTierMixin {
    @Inject(method = "productivityTier", at = @At("RETURN"), cancellable = true)
    private static void ftbarmory$metalTier(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValueI() == 0) {
            int tier = ProductiveBeesCompat.mystianTier(stack);
            if (tier > 0) {
                cir.setReturnValue(tier);
            }
        }
    }
}
