package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonArray;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class ProcessingRecipeJS extends RecipeJS {
    public final List<FluidIngredient> inputFluids = new ArrayList<>();
    public final List<FluidStackJS> outputFluids = new ArrayList<>();
    public final List<ItemStack> outputItems = new ArrayList<>();
    public final List<Ingredient> inputItems = new ArrayList<>();

    @Override
    public void create(RecipeArguments args) {
        for (var result : ListJS.orSelf(args.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                outputItems.add(parseItemOutput(result));
            }
        }

        for (var input : ListJS.orSelf(args.get(1))) {
            if (input instanceof FluidStackJS fluid) {
                inputFluids.add(FluidIngredientHelper.toFluidIngredient(fluid));
            } else if (input instanceof Map<?, ?> map && (map.containsKey("fluid") || map.containsKey("fluidTag"))) {
                inputFluids.add(FluidIngredient.deserialize(MapJS.json(input)));
            } else {
                inputItems.add(parseItemInput(input));
            }
        }

        json.addProperty("processingTime", 100);
    }

    @Override
    public void deserialize() {
        for (var ingredient : json.get("ingredients").getAsJsonArray()) {
            if (FluidIngredient.isFluidIngredient(ingredient)) {
                inputFluids.add(FluidIngredient.deserialize(ingredient));
            } else {
                inputItems.add(parseItemInput(ingredient));
            }
        }

        for (var result : json.get("results").getAsJsonArray()) {
            var resultJson = result.getAsJsonObject();

            if (resultJson.has("fluid")) {
                outputFluids.add(FluidStackJS.fromJson(resultJson));
            } else {
                outputItems.add(parseItemOutput(result));
            }
        }
    }

    public ProcessingRecipeJS processingTime(int t) {
        json.addProperty("processingTime", t);
        save();
        return this;
    }

    public ProcessingRecipeJS heatRequirement(String req) {
        json.addProperty("heatRequirement", req);
        save();
        return this;
    }

    public ProcessingRecipeJS heated() {
        return heatRequirement("heated");
    }

    public ProcessingRecipeJS superheated() {
        return heatRequirement("superheated");
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            var jsonIngredients = new JsonArray();
            for (var inputStack : inputItems) {
                jsonIngredients.add(inputStack.toJson());
            }
            for (var fluid : inputFluids) {
                jsonIngredients.add(fluid.serialize());
            }
            json.add("ingredients", jsonIngredients);
        }

        if (serializeOutputs) {
            var jsonOutputs = new JsonArray();
            for (var item : outputItems) {
                jsonOutputs.add(itemToJson(item));
            }
            for (var fluid : outputFluids) {
                jsonOutputs.add(fluid.toJson());
            }
            json.add("results", jsonOutputs);
        }
    }

    @Override
    public boolean hasInput(IngredientMatch match) {
        return inputItems.stream().anyMatch(match::contains);
    }

    @Override
    public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
        if (hasInput(match)) {
            for (int i = 0; i < inputItems.size(); i++) {
                var ingredient = inputItems.get(i);
                if (match.contains(ingredient)) {
                    inputItems.set(i, transformer.transform(this, match, ingredient, with));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasOutput(IngredientMatch match) {
        return outputItems.stream().anyMatch(match::contains);
    }

    @Override
    public boolean replaceOutput(IngredientMatch match, ItemStack with, ItemOutputTransformer transformer) {
        if (hasOutput(match)) {
            for (int i = 0; i < outputItems.size(); i++) {
                var outputItem = outputItems.get(i);
                if (match.contains(outputItem)) {
                    outputItems.set(i, transformer.transform(this, match, outputItem, with));
                }
            }
            return true;
        }
        return false;
    }
}
