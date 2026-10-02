package dev.ftb.mods.ftbarmory.registry;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.common.item.AiotItem;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeckoItem;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeckoLibIntegration;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeckoShieldItem;
import dev.ftb.mods.ftbarmory.integration.geckolib.GeoVisual;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FTBArmory.MOD_ID);

    static final String OLD_MATERIAL_NAMESPACE = "ftbmaterials";
    static final String OLD_GEAR_NAMESPACE = "ftb";

    private static final List<String> MATERIAL_TYPES = List.of(
            "ingot",
            "nugget",
            "raw_ore",
            "dust",
            "small_dust",
            "tiny_dust",
            "dirty_dust",
            "plate",
            "gear",
            "rod",
            "wire",
            "chunk",
            "clump",
            "cluster",
            "crystal",
            "shard");
    private static final List<ArmorType> ARMOR_TYPES =
            List.of(ArmorType.HELMET, ArmorType.CHESTPLATE, ArmorType.LEGGINGS, ArmorType.BOOTS);

    private static final Set<Metal> GLOWING_GEAR = EnumSet.of(Metal.ADAMANTITE, Metal.AETERNIUM, Metal.AURICHALCUM);
    private static final Set<Metal> JSON_MODEL_SHIELDS = EnumSet.of(Metal.AETERNIUM);

    private static final List<DeferredItem<? extends Item>> MATERIALS = new ArrayList<>();
    private static final List<DeferredItem<? extends Item>> COMBAT = new ArrayList<>();
    private static final List<DeferredItem<? extends Item>> TOOLS = new ArrayList<>();

    static {
        for (Metal metal : Metal.values()) {
            for (String type : MATERIAL_TYPES) {
                MATERIALS.add(alias(
                        ITEMS.registerItem(metal.id() + "_" + type, Item::new, Item.Properties::fireResistant),
                        OLD_MATERIAL_NAMESPACE));
            }
            tools(metal);
            armor(metal);
            shield(metal);
        }
    }

    private ModItems() {}

    private static GeoVisual visual(Metal metal, String model, String texture) {
        return new GeoVisual(
                FTBArmory.id(metal.id() + "_" + model),
                FTBArmory.id(metal.id() + "_" + texture),
                GLOWING_GEAR.contains(metal));
    }

    static <T extends Item> DeferredItem<T> alias(DeferredItem<T> item, String oldNamespace) {
        ITEMS.addAlias(
                Identifier.fromNamespaceAndPath(oldNamespace, item.getId().getPath()), item.getId());
        return item;
    }

    private static UnaryOperator<Item.Properties> finish(Metal metal) {
        return props -> {
            props.fireResistant();
            return metal.unbreakable() ? props.component(DataComponents.UNBREAKABLE, Unit.INSTANCE) : props;
        };
    }

    private static <T extends Item> DeferredItem<T> gear(
            List<DeferredItem<? extends Item>> list, DeferredItem<T> item) {
        list.add(alias(item, OLD_GEAR_NAMESPACE));
        return item;
    }

    private static void tools(Metal metal) {
        String name = metal.id();
        Metal.ToolStats stats = metal.toolStats();
        ToolMaterial tool = new ToolMaterial(
                ModTags.INCORRECT_FOR_ADAMANTITE_TOOL,
                stats.durability(),
                stats.speed(),
                stats.attackBonus(),
                stats.enchantment(),
                metal.ingots());
        UnaryOperator<Item.Properties> finish = finish(metal);

        gear(
                COMBAT,
                ITEMS.registerItem(
                        name + "_sword",
                        p -> new GeckoItem(p, visual(metal, "sword", "sword")),
                        p -> finish.apply(p.sword(tool, 3.0F, -2.4F))));
        gear(
                COMBAT,
                ITEMS.registerItem(
                        name + "_spear",
                        p -> new GeckoItem(p, visual(metal, "spear", "spear_model")),
                        p -> finish.apply(p.spear(tool, 1.15F, 1.2F, 0.4F, 2.5F, 9.0F, 5.5F, 5.1F, 8.75F, 4.6F))));
        COMBAT.add(ITEMS.registerItem(
                name + "_scythe",
                p -> new GeckoItem(p, visual(metal, "scythe", "scythe")),
                p -> finish.apply(p.tool(tool, BlockTags.MINEABLE_WITH_HOE, 5.0F, -3.0F, 0.0F)
                        .component(DataComponents.WEAPON, new Weapon(1)))));
        gear(TOOLS, ITEMS.registerItem(name + "_pickaxe", Item::new, p -> finish.apply(p.pickaxe(tool, 1.0F, -2.8F))));
        gear(TOOLS, ITEMS.registerItem(name + "_axe", p -> new AxeItem(tool, 5.0F, -3.0F, p), finish));
        gear(TOOLS, ITEMS.registerItem(name + "_shovel", p -> new ShovelItem(tool, 1.5F, -3.0F, p), finish));
        gear(TOOLS, ITEMS.registerItem(name + "_hoe", p -> new HoeItem(tool, -4.0F, 0.0F, p), finish));
        gear(
                TOOLS,
                ITEMS.registerItem(
                        name + "_aiot",
                        AiotItem::new,
                        p -> finish.apply(p.tool(tool, ModTags.MINEABLE_AIOT, 4.0F, -3.0F, 0.0F))));
    }

    private static void armor(Metal metal) {
        Metal.ArmorStats stats = metal.armorStats();
        ArmorMaterial netherite = ArmorMaterials.NETHERITE;
        ArmorMaterial material = new ArmorMaterial(
                stats.durability(),
                stats.defense(),
                stats.enchantment(),
                netherite.equipSound(),
                stats.toughness(),
                stats.knockbackResistance(),
                metal.ingots(),
                netherite.assetId());
        Function<Item.Properties, ? extends Item> factory =
                GeckoLibIntegration.armorFactory(FTBArmory.id(metal.id() + "_armor"));
        UnaryOperator<Item.Properties> finish = finish(metal);
        for (ArmorType type : ARMOR_TYPES) {
            gear(
                    COMBAT,
                    ITEMS.registerItem(
                            metal.id() + "_" + type.getSerializedName(),
                            factory,
                            p -> finish.apply(p.humanoidArmor(material, type))));
        }
    }

    private static void shield(Metal metal) {
        UnaryOperator<Item.Properties> finish = finish(metal);
        COMBAT.add(ITEMS.registerItem(
                metal.id() + "_shield",
                JSON_MODEL_SHIELDS.contains(metal)
                        ? ShieldItem::new
                        : p -> new GeckoShieldItem(p, visual(metal, "shield", "shield")),
                p -> finish.apply(p.durability(metal.toolStats().durability())
                        .repairable(metal.ingots())
                        .equippableUnswappable(EquipmentSlot.OFFHAND)
                        .delayedComponent(
                                DataComponents.BLOCKS_ATTACKS,
                                context -> new BlocksAttacks(
                                        0.25F,
                                        1.0F,
                                        List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                        new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                        Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                        Optional.of(SoundEvents.SHIELD_BLOCK),
                                        Optional.of(SoundEvents.SHIELD_BREAK)))
                        .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK))));
    }

    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            MATERIALS.forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            ModBlocks.STORAGE_ITEMS.forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            ModBlocks.ORE_ITEMS.forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            COMBAT.forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            TOOLS.forEach(event::accept);
        }
    }
}
