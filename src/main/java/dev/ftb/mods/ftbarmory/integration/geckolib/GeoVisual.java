package dev.ftb.mods.ftbarmory.integration.geckolib;

import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.renderer.GeoItemRenderer;
import dev.ftb.mods.ftbarmory.integration.geckolib.client.GeckoItemRenderers;
import net.minecraft.resources.Identifier;

public record GeoVisual(Identifier model, Identifier texture, boolean glow) {
    public GeoRenderProvider renderProvider() {
        return new GeoRenderProvider() {
            private GeoItemRenderer<?> renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (renderer == null) {
                    renderer = GeckoItemRenderers.create(GeoVisual.this);
                }
                return renderer;
            }
        };
    }
}
