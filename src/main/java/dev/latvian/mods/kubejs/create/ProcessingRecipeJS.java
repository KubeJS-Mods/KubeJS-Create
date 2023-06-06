package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonArray;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ProcessingRecipeJS extends RecipeJS {
	public final List<FluidIngredient> inputFluids = new ArrayList<>();
	public final List<FluidStackJS> outputFluids = new ArrayList<>();

	@Override
	public void create(ListJS args) {
		for (var result : ListJS.orSelf(args.get(0))) {
			if (result instanceof FluidStackJS fluid) {
				outputFluids.add(fluid);
			} else {
				outputItems.add(parseResultItem(result));
			}
		}

		for (var input : ListJS.orSelf(args.get(1))) {
			if (input instanceof FluidStackJS fluid) {
				inputFluids.add(FluidIngredientHelper.toFluidIngredient(fluid));
			} else if (input instanceof MapJS map && (map.containsKey("fluid") || map.containsKey("fluidTag"))) {
				inputFluids.add(FluidIngredient.deserialize(map.toJson()));
			} else {
				inputItems.add(parseIngredientItem(input));
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
				inputItems.add(parseIngredientItem(ingredient));
			}
		}

		for (var result : json.get("results").getAsJsonArray()) {
			var resultJson = result.getAsJsonObject();

			if (resultJson.has("fluid")) {
				outputFluids.add(FluidStackJS.fromJson(resultJson));
			} else {
				outputItems.add(parseResultItem(result));
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
		var jsonIngredients = new JsonArray();
		var jsonOutputs = new JsonArray();

		for (var inputStack : inputItems) {
			for (var ingredient : inputStack.unwrapStackIngredient()) {
				jsonIngredients.add(ingredient.toJson());
			}
		}

		for (var fluid : inputFluids) {
			jsonIngredients.add(fluid.serialize());
		}

		for (var item : outputItems) {
			jsonOutputs.add(item.toResultJson());
		}

		for (var fluid : outputFluids) {
			jsonOutputs.add(fluid.toJson());
		}

		json.add("ingredients", jsonIngredients);
		json.add("results", jsonOutputs);
	}
}
