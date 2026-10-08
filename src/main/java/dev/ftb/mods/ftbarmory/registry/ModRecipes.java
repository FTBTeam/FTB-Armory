package dev.ftb.mods.ftbarmory.registry;

import dev.ftb.mods.ftbarmory.FTBArmory;
import dev.ftb.mods.ftbarmory.integration.hostilenetworks.DataModelTierRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FTBArmory.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DataModelTierRecipe>> DATA_MODEL_TIER =
            SERIALIZERS.register(
                    "data_model_tier",
                    () -> new RecipeSerializer<>(DataModelTierRecipe.MAP_CODEC, DataModelTierRecipe.STREAM_CODEC));

    private ModRecipes() {}
}
