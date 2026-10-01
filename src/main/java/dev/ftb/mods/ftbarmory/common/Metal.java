package dev.ftb.mods.ftbarmory.common;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public enum Metal {
    ADAMANTITE(
            MapColor.COLOR_CYAN,
            List.of(
                    new Ore("stone_ore", 8.0F, SoundType.STONE, MapColor.STONE),
                    new Ore("deepslate_ore", 9.5F, SoundType.DEEPSLATE, MapColor.DEEPSLATE)),
            new ToolStats(3072, 10.0F, 7.0F, 12),
            new ArmorStats(56, 5, 10, 8, 5, 12, 5.0F, 0.15F),
            false),
    AETERNIUM(
            MapColor.COLOR_PURPLE,
            List.of(new Ore("nether_ore", 10.0F, SoundType.NETHER_ORE, MapColor.NETHER)),
            new ToolStats(2560, 11.0F, 9.0F, 25),
            new ArmorStats(47, 5, 10, 8, 5, 25, 4.0F, 0.1F),
            false),
    AURICHALCUM(
            MapColor.COLOR_ORANGE,
            List.of(new Ore("end_ore", 12.0F, SoundType.STONE, MapColor.SAND)),
            new ToolStats(4096, 12.0F, 13.0F, 20),
            new ArmorStats(75, 7, 12, 10, 7, ArmorMaterials.NETHERITE.enchantmentValue(), 7.0F, 0.2F),
            true);

    private final MapColor color;
    private final List<Ore> ores;
    private final ToolStats toolStats;
    private final ArmorStats armorStats;
    private final boolean unbreakable;

    Metal(MapColor color, List<Ore> ores, ToolStats toolStats, ArmorStats armorStats, boolean unbreakable) {
        this.color = color;
        this.ores = ores;
        this.toolStats = toolStats;
        this.armorStats = armorStats;
        this.unbreakable = unbreakable;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public MapColor color() {
        return color;
    }

    public List<Ore> ores() {
        return ores;
    }

    public ToolStats toolStats() {
        return toolStats;
    }

    public ArmorStats armorStats() {
        return armorStats;
    }

    public boolean unbreakable() {
        return unbreakable;
    }

    public TagKey<Item> ingots() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/" + id()));
    }

    public record Ore(String suffix, float hardness, SoundType sound, MapColor color) {}

    public record ToolStats(int durability, float speed, float attackBonus, int enchantment) {}

    public record ArmorStats(
            int durability,
            int helmet,
            int chestplate,
            int leggings,
            int boots,
            int enchantment,
            float toughness,
            float knockbackResistance) {
        public Map<ArmorType, Integer> defense() {
            Map<ArmorType, Integer> defense = new EnumMap<>(ArmorMaterials.NETHERITE.defense());
            defense.put(ArmorType.HELMET, helmet);
            defense.put(ArmorType.CHESTPLATE, chestplate);
            defense.put(ArmorType.LEGGINGS, leggings);
            defense.put(ArmorType.BOOTS, boots);
            return defense;
        }
    }
}
