package dev.ftb.mods.ftbarmory.registry;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.common.recipe.CountedSmithingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FTBArmory.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CountedSmithingRecipe>> COUNTED_SMITHING =
            SERIALIZERS.register(
                    "counted_smithing",
                    () -> new RecipeSerializer<>(CountedSmithingRecipe.MAP_CODEC, CountedSmithingRecipe.STREAM_CODEC));

    private ModRecipes() {}
}
