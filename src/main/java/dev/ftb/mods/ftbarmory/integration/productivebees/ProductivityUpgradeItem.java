package dev.ftb.mods.ftbarmory.integration.productivebees;

import cy.jdkdigital.productivelib.common.item.UpgradeItem;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class ProductivityUpgradeItem extends UpgradeItem {
    public final double productivity;

    public ProductivityUpgradeItem(Properties properties, double productivity) {
        super(properties);
        this.productivity = productivity;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            TooltipDisplay display,
            Consumer<Component> builder,
            TooltipFlag flag) {
        builder.accept(Component.translatable(
                        "item.ftbarmory.productivity_upgrade.tooltip", String.format("%.1f", productivity))
                .withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
