package dev.ftb.mods.ftbarmory.integration.geckolib.client;

import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoArmorRenderer;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeckoArmorItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public final class GeckoArmorRenderer extends GeoArmorRenderer<GeckoArmorItem, HumanoidRenderState> {
    public GeckoArmorRenderer(Identifier model) {
        super(new DefaultedItemGeoModel<>(model));
    }
}
