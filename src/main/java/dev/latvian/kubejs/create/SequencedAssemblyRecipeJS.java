package dev.latvian.kubejs.create;

import com.google.gson.JsonArray;
import com.simibubi.create.content.contraptions.itemAssembly.IAssemblyRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.world.item.crafting.Recipe;

public class SequencedAssemblyRecipeJS extends RecipeJS {
	@Override
	public void create(ListJS args) {
		outputItems.addAll(parseResultItemList(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)));
		json.add("transitionalItem", ItemStackJS.of("create:precision_mechanism").toResultJson());
		json.addProperty("loops", 4);

		JsonArray sequence = new JsonArray();

		for (Object o : ListJS.orSelf(args.get(2))) {
			if (o instanceof RecipeJS) {
				((RecipeJS) o).dontAdd();

				try {
					Recipe<?> r = ((RecipeJS) o).createRecipe();

					if (r instanceof IAssemblyRecipe && r instanceof ProcessingRecipe && ((IAssemblyRecipe) r).supportsAssembly()) {
						sequence.add(new SequencedRecipe<>((ProcessingRecipe<?>) r).toJson());
					} else {
						throw new RecipeExceptionJS("Sequence recipe must be an assembly recipe!");
					}
				} catch (Throwable ex) {
					throw new RecipeExceptionJS("Failed to create " + ex);
				}
			} else {
				throw new RecipeExceptionJS("Object must be a recipe, instead got " + o + " / " + o.getClass().getName());
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
			JsonArray array = new JsonArray();

			for (ItemStackJS out : outputItems) {
				array.add(out.toResultJson());
			}

			json.add("results", array);
		}

		if (serializeInputs) {
			json.add("ingredient", inputItems.get(0).toJson());
		}
	}
}
