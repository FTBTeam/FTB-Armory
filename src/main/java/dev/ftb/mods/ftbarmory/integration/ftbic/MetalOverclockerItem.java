package dev.ftb.mods.ftbarmory.integration.ftbic;

import dev.ftb.mods.ftbic.item.UpgradeItem;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class MetalOverclockerItem extends UpgradeItem {
    public final double speed;
    public final double energy;

    public MetalOverclockerItem(Properties properties, double speed, double energy) {
        super(properties, 16);
        this.speed = speed;
        this.energy = energy;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            TooltipDisplay display,
            Consumer<Component> builder,
            TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("item.ftbarmory.overclocker.speed", String.format("%.2f", speed))
                .withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("item.ftbarmory.overclocker.energy", String.format("%.2f", energy))
                .withStyle(ChatFormatting.GRAY));
        builder.accept(
                Component.translatable("item.ftbarmory.overclocker.limit").withStyle(ChatFormatting.DARK_GRAY));
    }
}
