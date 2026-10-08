package dev.ftb.mods.ftbarmory.integration.thaumaturge;

import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.essentia.smeltery.BlockSmelter;
import com.leclowndu93150.thaumaturge.content.infusion.BlockPedestal;
import com.leclowndu93150.thaumaturge.content.infusion.BlockPillar;
import com.leclowndu93150.thaumaturge.content.spell.item.FocusItem;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandCap;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandRod;
import com.leclowndu93150.thaumaturge.registry.TTBlockEntities;
import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModBlocks;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ThaumaturgeCompat {
    public static final DeferredRegister<WandCap> CAPS =
            DeferredRegister.create(WandCap.REGISTRY_KEY, FTBArmory.MOD_ID);
    public static final DeferredRegister<WandRod> RODS =
            DeferredRegister.create(WandRod.REGISTRY_KEY, FTBArmory.MOD_ID);

    private static DeferredBlock<BlockPedestal> pedestal;
    private static DeferredBlock<BlockSmelter> smelter;

    private ThaumaturgeCompat() {}

    public static void init(IEventBus eventBus) {
        DeferredBlock<BlockPillar> pillar = ModBlocks.BLOCKS.registerBlock(
                "aurichalcum_pillar",
                BlockPillar::new,
                props -> props.mapColor(MapColor.COLOR_ORANGE)
                        .strength(2.0F, 17.5F)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        .requiresCorrectToolForDrops());
        pedestal = ModBlocks.BLOCKS.registerBlock(
                "aurichalcum_pedestal",
                props -> new BlockPedestal(BlockPedestal.Variant.ARCANE, props),
                props -> props.mapColor(MapColor.COLOR_ORANGE)
                        .strength(2.0F, 17.5F)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        .requiresCorrectToolForDrops());
        smelter = ModBlocks.BLOCKS.registerBlock(
                "aeternium_smelter",
                BlockSmelter::new,
                props -> props.mapColor(MapColor.COLOR_PURPLE)
                        .strength(2.0F, 20.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .lightLevel(state -> state.getValue(BlockSmelter.LIT) ? 13 : 0));
        blockItem("aurichalcum_pillar", pillar);
        blockItem("aurichalcum_pedestal", pedestal);
        blockItem("aeternium_smelter", smelter);

        Compat.add(ModItems.ITEMS.registerItem(
                "aurichalcum_focus",
                FocusItem::new,
                props -> props.fireResistant().stacksTo(1).rarity(Rarity.EPIC)));

        wandParts(Metal.ADAMANTITE, 0.73F, 9, 110, 275, 24);
        wandParts(Metal.AETERNIUM, 0.60F, 10, 150, 375, 26);
        wandParts(Metal.AURICHALCUM, 0.73F, 11, 245, 610, 28);

        CAPS.register(eventBus);
        RODS.register(eventBus);
        eventBus.addListener(ThaumaturgeCompat::addBlocks);
    }

    private static void wandParts(
            Metal metal, float costModifier, int partCost, int wandCapacity, int staffCapacity, int staffCost) {
        String name = metal.id();
        Identifier capTexture = FTBArmory.id("textures/models/wand_cap_" + name + ".png");
        Identifier rodTexture = FTBArmory.id("textures/models/wand_rod_" + name + ".png");
        Identifier research = FTBArmory.id("esoteric_metallurgy");
        DeferredHolder<WandCap, WandCap> cap =
                CAPS.register(name, () -> new WandCap(costModifier, List.of(), 0.0F, partCost, capTexture));
        DeferredHolder<WandRod, WandRod> wandRod = RODS.register(
                name,
                () -> new WandRod(
                        wandCapacity, partCost, rodTexture, (stack, player) -> {}, false, false, false, research));
        DeferredHolder<WandRod, WandRod> staffRod = RODS.register(
                name + "_staff",
                () -> new WandRod(
                        staffCapacity, staffCost, rodTexture, (stack, player) -> {}, false, true, false, research));
        Compat.add(ModItems.ITEMS.registerItem(
                name + "_wand_cap", props -> new ItemWandCap(props, cap), props -> props.fireResistant()));
        Compat.add(ModItems.ITEMS.registerItem(
                name + "_wand_rod", props -> new ItemWandRod(props, wandRod), props -> props.fireResistant()));
        Compat.add(ModItems.ITEMS.registerItem(
                name + "_staff_rod", props -> new ItemWandRod(props, staffRod), props -> props.fireResistant()));
    }

    private static <B extends Block> void blockItem(String name, DeferredBlock<B> block) {
        Compat.add(ModItems.ITEMS.registerItem(
                name,
                props -> new BlockItem(block.get(), props),
                props -> props.useBlockDescriptionPrefix().fireResistant()));
    }

    private static void addBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(TTBlockEntities.PEDESTAL.getKey(), pedestal.get());
        event.modify(TTBlockEntities.SMELTER.getKey(), smelter.get());
    }
}
