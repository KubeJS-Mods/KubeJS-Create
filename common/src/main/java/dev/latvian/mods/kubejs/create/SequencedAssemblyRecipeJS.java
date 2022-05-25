package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonArray;
import com.simibubi.create.content.contraptions.itemAssembly.IAssemblyRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.world.item.crafting.Recipe;

public class SequencedAssemblyRecipeJS extends RecipeJS {
	@Override
	public void create(ListJS args) {
		outputItems.addAll(parseResultItemList(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)));
		json.add("transitionalItem", ItemStackJS.of("create:precision_mechanism").toResultJson());
		json.addProperty("loops", 4);

		var sequence = new JsonArray();

		for (var step : ListJS.orSelf(args.get(2))) {
			if (step instanceof RecipeJS recipeJS) {
				recipeJS.dontAdd();

				try {
					var recipe = recipeJS.createRecipe();

					if (recipe instanceof IAssemblyRecipe ass && recipe instanceof ProcessingRecipe proc && ass.supportsAssembly()) {
						sequence.add(new SequencedRecipe<>(proc).toJson());
					} else {
						throw new RecipeExceptionJS("Sequence recipe must be an assembly recipe!");
					}
				} catch (Throwable ex) {
					throw new RecipeExceptionJS("Failed to create " + ex);
				}
			} else {
				throw new RecipeExceptionJS("Object must be a recipe, instead got " + step + " / " + step.getClass().getName());
			}
		}

		json.add("sequence", sequence);
	}

	@Override
	public void deserialize() {
		outputItems.addAll(parseResultItemList(json.get("results")));
		inputItems.add(parseIngredientItem(json.get("ingredient")));
	}

	public SequencedAssemblyRecipeJS transitionalItem(ItemStackJS item) {
		json.add("transitionalItem", item.toResultJson());
		save();
		return this;
	}

	public SequencedAssemblyRecipeJS loops(int loops) {
		json.addProperty("loops", loops);
		save();
		return this;
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			var results = new JsonArray();

			for (var out : outputItems) {
				results.add(out.toResultJson());
			}

			json.add("results", results);
		}

		if (serializeInputs) {
			json.add("ingredient", inputItems.get(0).toJson());
		}
	}
}
