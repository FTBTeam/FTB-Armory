package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.productivebees.ProductiveBeesCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.mystian.apiary.bee.PBIntegration", remap = false)
public abstract class MystianApiaryMultiplierMixin {
    @Inject(method = "getProductivityMultiplier", at = @At("HEAD"), cancellable = true)
    private static void ftbarmory$metalMultiplier(int tier, CallbackInfoReturnable<Double> cir) {
        double multiplier = ProductiveBeesCompat.mystianMultiplier(tier);
        if (multiplier > 0.0) {
            cir.setReturnValue(multiplier);
        }
    }
}
