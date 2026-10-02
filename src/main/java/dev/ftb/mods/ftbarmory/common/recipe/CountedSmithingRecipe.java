package dev.ftb.mods.ftbarmory.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbarmory.registry.ModRecipes;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.level.Level;

public record CountedSmithingRecipe(SmithingTransformRecipe transform, int additionCount) implements SmithingRecipe {
    public static final MapCodec<CountedSmithingRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                            SmithingTransformRecipe.MAP_CODEC.forGetter(CountedSmithingRecipe::transform),
                            Codec.intRange(1, 64)
                                    .fieldOf("addition_count")
                                    .forGetter(CountedSmithingRecipe::additionCount))
                    .apply(instance, CountedSmithingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CountedSmithingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    SmithingTransformRecipe.STREAM_CODEC,
                    CountedSmithingRecipe::transform,
                    ByteBufCodecs.VAR_INT,
                    CountedSmithingRecipe::additionCount,
                    CountedSmithingRecipe::new);

    public CountedSmithingRecipe {
        if (additionCount < 1
                || additionCount > 64
                || transform.additionIngredient().isEmpty()) {
            throw new IllegalArgumentException(
                    "Counted smithing requires an addition ingredient and a count from 1 to 64");
        }
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        return input.addition().getCount() >= additionCount && transform.matches(input, level);
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        return transform.assemble(input);
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return transform.templateIngredient();
    }

    @Override
    public Ingredient baseIngredient() {
        return transform.baseIngredient();
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return transform.additionIngredient();
    }

    @Override
    public RecipeSerializer<CountedSmithingRecipe> getSerializer() {
        return ModRecipes.COUNTED_SMITHING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return transform.placementInfo();
    }

    @Override
    public boolean showNotification() {
        return transform.showNotification();
    }

    @Override
    public String group() {
        return transform.group();
    }

    @Override
    public List<RecipeDisplay> display() {
        SmithingRecipeDisplay display =
                (SmithingRecipeDisplay) transform.display().getFirst();
        List<SlotDisplay> additions = additionIngredient()
                .orElseThrow()
                .items()
                .<SlotDisplay>map(
                        item -> new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(item, additionCount)))
                .toList();
        return List.of(new SmithingRecipeDisplay(
                display.template(),
                display.base(),
                new SlotDisplay.Composite(additions),
                display.result(),
                display.craftingStation()));
    }
}
