package dev.ftb.mods.ftbarmory.integration.oritech;

import dev.ftb.mods.ftbarmory.common.Metal;
import dev.ftb.mods.ftbarmory.integration.Compat;
import dev.ftb.mods.ftbarmory.registry.ModBlocks;
import dev.ftb.mods.ftbarmory.registry.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import rearth.oritech.block.blocks.processing.MachineCoreBlock;

public final class OritechCompat {
    private static final ResourceKey<net.minecraft.world.level.block.entity.BlockEntityType<?>> MACHINE_CORE =
            ResourceKey.create(
                    Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath("oritech", "machine_core"));
    private static final List<DeferredBlock<MachineCoreBlock>> CORES = new ArrayList<>();

    private OritechCompat() {}

    public static void init(IEventBus eventBus) {
        core(Metal.ADAMANTITE, 8);
        core(Metal.AETERNIUM, 10);
        core(Metal.AURICHALCUM, 12);
        eventBus.addListener(OritechCompat::addBlocks);
    }

    private static void core(Metal metal, int level) {
        String name = metal.id() + "_machine_core";
        DeferredBlock<MachineCoreBlock> block = ModBlocks.BLOCKS.registerBlock(
                name,
                props -> new MachineCoreBlock(props, level),
                props -> props.mapColor(metal.color())
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .noOcclusion());
        CORES.add(block);
        Compat.add(ModItems.ITEMS.registerItem(
                name,
                props -> new BlockItem(block.get(), props) {
                    @Override
                    public void appendHoverText(
                            ItemStack stack,
                            TooltipContext context,
                            TooltipDisplay display,
                            Consumer<Component> builder,
                            TooltipFlag flag) {
                        super.appendHoverText(stack, context, display, builder, flag);
                        block.get().addToTooltip(context, builder, flag, stack);
                    }
                },
                props -> props.useBlockDescriptionPrefix().fireResistant()));
    }

    private static void addBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(MACHINE_CORE, CORES.stream().map(DeferredBlock::get).toArray(Block[]::new));
    }
}
