package dev.ftb.mods.ftbarmory.integration.geckolib;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoArmorRenderer;
import dev.ftb.mods.ftbarmory.integration.geckolib.client.GeckoArmorRenderer;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GeckoArmorItem extends Item implements GeoItem {
    private final AnimatableInstanceCache geoCache = new SingletonAnimatableInstanceCache(this);
    private final Identifier model;

    public GeckoArmorItem(Properties properties, Identifier model) {
        super(properties);
        this.model = model;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeckoArmorRenderer renderer;

            @Override
            public GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack stack, EquipmentSlot slot) {
                if (renderer == null) {
                    renderer = new GeckoArmorRenderer(model);
                }
                return renderer;
            }
        });
    }
}
