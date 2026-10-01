package dev.ftb.mods.ftbarmory.registry;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.Metal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FTBArmory.MOD_ID);

    static final List<DeferredItem<BlockItem>> STORAGE_ITEMS = new ArrayList<>();
    static final List<DeferredItem<BlockItem>> ORE_ITEMS = new ArrayList<>();

    static {
        for (Metal metal : Metal.values()) {
            STORAGE_ITEMS.add(registerBlock(
                    metal.id() + "_block",
                    props -> props.mapColor(metal.color())
                            .strength(50.0F, 1200.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)));
            STORAGE_ITEMS.add(registerBlock(
                    metal.id() + "_raw_block",
                    props -> props.mapColor(metal.color())
                            .strength(10.0F, 12.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)));
            for (Metal.Ore ore : metal.ores()) {
                ORE_ITEMS.add(registerBlock(
                        metal.id() + "_" + ore.suffix(),
                        props -> props.mapColor(ore.color())
                                .strength(ore.hardness(), 12.0F)
                                .requiresCorrectToolForDrops()
                                .sound(ore.sound())));
            }
        }
    }

    private ModBlocks() {}

    private static DeferredItem<BlockItem> registerBlock(
            String name, UnaryOperator<BlockBehaviour.Properties> properties) {
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, Block::new, properties);
        BLOCKS.addAlias(Identifier.fromNamespaceAndPath(ModItems.OLD_MATERIAL_NAMESPACE, name), block.getId());
        DeferredItem<BlockItem> item = ModItems.ITEMS.registerItem(
                name,
                props -> new BlockItem(block.get(), props),
                props -> props.useBlockDescriptionPrefix().fireResistant());
        ModItems.alias(item, ModItems.OLD_MATERIAL_NAMESPACE);
        return item;
    }
}
