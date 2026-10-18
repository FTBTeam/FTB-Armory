package dev.ftb.mods.ftbarmory.integration.apotheosis;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.shadowsoffire.apotheosis.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.affix.effect.OmneticAffix;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.util.OmneticUtil;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.state.BlockState;

public final class OmneticHarvest {
    private static final Field VALUES = valuesField();

    private OmneticHarvest() {}

    public static boolean allows(ItemStack stack, BlockState state) {
        if (VALUES == null || stack.isEmpty()) {
            return false;
        }
        AffixInstance inst = AffixHelper.streamAffixes(stack)
                .filter(i -> i.getAffix() instanceof OmneticAffix)
                .findFirst()
                .orElse(null);
        if (inst == null || !inst.isValid()) {
            return false;
        }
        OmneticUtil.OmneticData data = values(inst.getAffix()).get(inst.rarity().get());
        if (data == null) {
            return false;
        }
        for (ItemStackTemplate template : data.items()) {
            if (template.create().isCorrectToolForDrops(state)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static Map<LootRarity, OmneticUtil.OmneticData> values(Object affix) {
        try {
            return (Map<LootRarity, OmneticUtil.OmneticData>) VALUES.get(affix);
        } catch (IllegalAccessException e) {
            return Map.of();
        }
    }

    private static Field valuesField() {
        try {
            Field field = OmneticAffix.class.getDeclaredField("values");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException | RuntimeException e) {
            FTBArmory.LOGGER.error(
                    "Could not read Apotheosis omnetic values; omnetic tools cannot mine protected ores", e);
            return null;
        }
    }
}
