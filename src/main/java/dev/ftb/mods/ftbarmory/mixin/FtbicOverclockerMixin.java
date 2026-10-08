package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.ftbic.FtbicOverclockers;
import dev.ftb.mods.ftbic.block.entity.machine.BasicMachineBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbic.block.entity.machine.BasicMachineBlockEntity", remap = false)
public abstract class FtbicOverclockerMixin {
    @Inject(method = "upgradesChanged()V", at = @At("TAIL"))
    private void ftbarmory$metalOverclockers(CallbackInfo ci) {
        FtbicOverclockers.apply((BasicMachineBlockEntity) (Object) this);
    }
}
