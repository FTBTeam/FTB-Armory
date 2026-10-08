package dev.ftb.mods.ftbarmory.integration.artifice;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ComponentType;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import io.redspace.irons_artifice.modifier.ModifierItem;
import io.redspace.irons_artifice.modifier.ValueStackModifier;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class ArtificeCompat {
    private static final ComponentType<Boolean> ARMORY_ROUNDS =
            new ComponentType<>(FTBArmory.id("armory_rounds"), () -> false);

    private ArtificeCompat() {}

    public static void init() {
        register(Metal.ADAMANTITE, 0.30, 1, 0x93B3B1, 0x22363A);
        register(Metal.AETERNIUM, 0.45, 2, 0xCB9BF0, 0x461B6A);
        register(Metal.AURICHALCUM, 0.65, 3, 0xFBD174, 0x9A360C);
    }

    private static void register(Metal metal, double damage, int pierce, int trailFrom, int trailTo) {
        ValueStackModifier stats = new ValueStackModifier(Map.of(
                ShotComponents.DAMAGE,
                new ValueModifier(damage, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL),
                ShotComponents.PIERCING,
                new ValueModifier(pierce, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL)));
        GunModifier modifier = new GunModifier() {
            @Override
            public void apply(ShotComponentMap components) {
                if (components.has(ARMORY_ROUNDS)) {
                    return;
                }
                components.set(ARMORY_ROUNDS, true);
                stats.apply(components);
                components
                        .getOrCreate(ShotComponents.PARTICLE_TRAIL)
                        .add(ColorTransitionParticleOption.bulletTrail(trailFrom, trailTo));
            }

            @Override
            public void getDescriptionText(Consumer<Component> builder) {
                stats.getDescriptionText(builder);
                builder.accept(Component.translatable("item.ftbarmory.armory_rounds.tooltip")
                        .withStyle(ChatFormatting.GRAY));
            }
        };
        Compat.add(ModItems.ITEMS.registerItem(
                metal.id() + "_rounds",
                props -> new ModifierItem(props, modifier),
                props -> props.fireResistant().stacksTo(1)));
    }
}
