package dev.ftb.mods.ftbarmory.integration.geckolib;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;

public class GeckoItem extends Item implements GeoItem {
    private final AnimatableInstanceCache geoCache = new SingletonAnimatableInstanceCache(this);
    private final GeoVisual visual;

    public GeckoItem(Properties properties, GeoVisual visual) {
        super(properties);
        this.visual = visual;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(visual.renderProvider());
    }
}
