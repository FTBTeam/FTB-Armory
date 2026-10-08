package dev.ftb.mods.ftbarmory.integration.hostilenetworks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbarmory.registry.ModRecipes;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class DataModelTierRecipe extends ShapelessRecipe {
    private static final Identifier DATA_MODEL = Identifier.fromNamespaceAndPath("hostilenetworks", "data_model");
    private static final Identifier DATA = Identifier.fromNamespaceAndPath("hostilenetworks", "data");

    public static final MapCodec<DataModelTierRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
                            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(recipe -> recipe.bookInfo),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                            Ingredient.CODEC
                                    .listOf(1, 9)
                                    .fieldOf("ingredients")
                                    .forGetter(recipe -> recipe.ingredients),
                            Codec.INT.fieldOf("min_data").forGetter(recipe -> recipe.minData),
                            Codec.INT.fieldOf("set_data").forGetter(recipe -> recipe.setData))
                    .apply(instance, DataModelTierRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DataModelTierRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            recipe -> recipe.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            recipe -> recipe.bookInfo,
            ItemStackTemplate.STREAM_CODEC,
            recipe -> recipe.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            recipe -> recipe.ingredients,
            ByteBufCodecs.VAR_INT,
            recipe -> recipe.minData,
            ByteBufCodecs.VAR_INT,
            recipe -> recipe.setData,
            DataModelTierRecipe::new);

    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients;
    private final int minData;
    private final int setData;

    public DataModelTierRecipe(
            Recipe.CommonInfo commonInfo,
            CraftingRecipe.CraftingBookInfo bookInfo,
            ItemStackTemplate result,
            List<Ingredient> ingredients,
            int minData,
            int setData) {
        super(commonInfo, bookInfo, result, ingredients);
        this.result = result;
        this.ingredients = ingredients;
        this.minData = minData;
        this.setData = setData;
    }

    @SuppressWarnings("unchecked")
    private static DataComponentType<Integer> dataComponent() {
        return (DataComponentType<Integer>) BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(DATA);
    }

    private static ItemStack findModel(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (!stack.isEmpty() && DATA_MODEL.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!super.matches(input, level)) {
            return false;
        }
        DataComponentType<Integer> component = dataComponent();
        ItemStack model = findModel(input);
        if (component == null || model.isEmpty()) {
            return false;
        }
        int data = model.getOrDefault(component, 0);
        return data >= minData && data < setData;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        DataComponentType<Integer> component = dataComponent();
        ItemStack model = findModel(input);
        if (component == null || model.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack upgraded = model.copyWithCount(1);
        upgraded.set(component, setData);
        return upgraded;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) ModRecipes.DATA_MODEL_TIER.get();
    }
}
