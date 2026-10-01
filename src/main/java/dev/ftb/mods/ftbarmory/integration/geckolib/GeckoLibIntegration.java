package dev.ftb.mods.ftbarmory.integration.geckolib;

import java.util.function.Function;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class GeckoLibIntegration {
    private GeckoLibIntegration() {}

    public static Function<Item.Properties, Item> armorFactory(Identifier model) {
        return properties -> new GeckoArmorItem(properties, model);
    }
}
