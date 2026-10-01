package dev.ftb.mods.ftbarmory.registry;

import dev.ftb.mods.ftbarmory.FTBArmory;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final TagKey<Block> NEEDS_ADAMANTITE_TOOL = block("needs_adamantite_tool");
    public static final TagKey<Block> INCORRECT_FOR_ADAMANTITE_TOOL = block("incorrect_for_adamantite_tool");
    public static final TagKey<Block> MINEABLE_AIOT = block("mineable/aiot");

    private ModTags() {}

    private static TagKey<Block> block(String path) {
        return TagKey.create(Registries.BLOCK, FTBArmory.id(path));
    }
}
