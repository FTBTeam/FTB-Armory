package dev.ftb.mods.ftbarmory.integration.geckolib.client;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeoVisual;
import net.minecraft.world.item.Item;

public final class GeckoItemRenderers {
    private GeckoItemRenderers() {}

    public static <T extends Item & GeoAnimatable> GeoItemRenderer<T> create(GeoVisual visual) {
        GeoItemRenderer<T> renderer =
                new GeoItemRenderer<>(new DefaultedItemGeoModel<T>(visual.model()).withAltTexture(visual.texture()));
        return visual.glow() ? renderer.withRenderLayer(AutoGlowingGeoLayer::new) : renderer;
    }
}
