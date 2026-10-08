package dev.ftb.mods.ftbarmory.mixin;

import dev.ftb.mods.ftbarmory.integration.productivebees.ProductiveBeesCompat;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "cy.jdkdigital.productivebees.common.block.entity.AdvancedBeehiveBlockEntity", remap = false)
public abstract class ProductiveBeesHiveMixin {
    @ModifyArg(
            method = "beeReleasePostAction",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcy/jdkdigital/productivebees/util/BeeHelper;getBeeProduce(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/animal/bee/Bee;ZD)Ljava/util/List;"),
            index = 3)
    private double ftbarmory$metalProductivity(double productivity) {
        return productivity + ProductiveBeesCompat.bonusProductivity((BlockEntity) (Object) this);
    }
}
