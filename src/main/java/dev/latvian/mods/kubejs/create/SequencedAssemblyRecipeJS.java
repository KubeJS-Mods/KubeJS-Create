package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonArray;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SequencedAssemblyRecipeJS extends RecipeJS {

	public final List<ItemStack> outputItems = new ArrayList<>();
	public Ingredient ingredient;

	@Override
	public void create(RecipeArguments args) {
		outputItems.addAll(parseItemOutputList(args.get(0)));
		ingredient = parseItemInput(args.get(1));
		json.add("transitionalItem", ItemStackJS.of("create:precision_mechanism").toJsonJS());
		json.addProperty("loops", 4);

		var sequence = new JsonArray();

		for (var step : ListJS.orSelf(args.get(2))) {
			if (step instanceof RecipeJS recipeJS) {
				recipeJS.dontAdd();
				try {
					var recipe = recipeJS.createRecipe();
					if (recipe instanceof IAssemblyRecipe ass && recipe instanceof ProcessingRecipe<?> proc && ass.supportsAssembly()) {
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
		outputItems.addAll(parseItemOutputList(json.get("results")));
		ingredient = parseItemInput(json.get("ingredient"));
	}

	public SequencedAssemblyRecipeJS transitionalItem(ItemStack item) {
		json.add("transitionalItem", item.toJsonJS());
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
				results.add(out.toJsonJS());
			}

			json.add("results", results);
		}

		if (serializeInputs) {
			json.add("ingredient", ingredient.toJson());
		}
	}

	@Override
	public boolean hasInput(IngredientMatch match) {
		return match.contains(ingredient);
	}

	@Override
	public boolean replaceInput(IngredientMatch match, Ingredient with, ItemInputTransformer transformer) {
		if (match.contains(ingredient)) {
			ingredient = transformer.transform(this, match, ingredient, with);
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
		}
		return false;
	}
}
