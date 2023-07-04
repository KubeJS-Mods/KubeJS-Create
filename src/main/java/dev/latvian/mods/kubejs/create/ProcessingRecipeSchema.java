package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public interface ProcessingRecipeSchema {

	RecipeKey<Either<OutputFluid, OutputItem>[]> RESULTS = FluidComponents.OUTPUT_OR_ITEM_ARRAY.key("results");
	RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");

	RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime").optional(100);

	RecipeKey<String> HEAT_REQUIREMENT = new StringComponent("not a valid heat condition!", s -> {
		for (var h : HeatCondition.values()) {
			if (h.name().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}).key("heatRequirement").defaultOptional().allowEmpty();

	// specifically used in item application
	RecipeKey<Boolean> KEEP_HELD_ITEM = BooleanComponent.BOOLEAN.key("keepHeldItem").optional(false);

	class ProcessingRecipeJS extends RecipeJS {

		@Override
		public InputFluid readInputFluid(Object from) {
			if (from instanceof CreateInputFluid fluid) {
				return fluid;
			} else if (from instanceof FluidIngredient fluid) {
				return new CreateInputFluid(fluid);
			} else if (from instanceof FluidStackJS fluid) {
				return new CreateInputFluid(FluidIngredient.fromFluid(fluid.getFluid(), (int) fluid.getAmount()));
			} else if (from instanceof FluidStack fluid) {
				return new CreateInputFluid(FluidIngredient.fromFluidStack(fluid));
			} else {
				var json = MapJS.json(from);
				if (json != null) {
					return new CreateInputFluid(FluidIngredient.deserialize(json));
				}
				return CreateInputFluid.EMPTY;
			}
		}

		@Override
		public JsonElement writeInputFluid(InputFluid value) {
			if (value instanceof CreateInputFluid fluid) {
				return fluid.ingredient().serialize();
			} else if (value instanceof FluidIngredient fluid) {
				return fluid.serialize();
			} else {
				return FluidIngredient.EMPTY.serialize();
			}
		}

		@Override
		public boolean inputFluidHasPriority(Object from) {
			return from instanceof InputFluid || FluidIngredient.isFluidIngredient(MapJS.json(from));
		}

		@Override
		public OutputItem readOutputItem(Object from) {
			if (from instanceof ProcessingOutput output) {
				return OutputItem.of(output.getStack(), output.getChance());
			} else {
				var outputItem = super.readOutputItem(from);
				if (from instanceof JsonObject j && j.has("chance")) {
					return outputItem.withChance(j.get("chance").getAsFloat());
				}
				return outputItem;
			}
		}

		public RecipeJS heated() {
			return setValue(HEAT_REQUIREMENT, HeatCondition.HEATED.serialize());
		}

		public RecipeJS superheated() {
			return setValue(HEAT_REQUIREMENT, HeatCondition.SUPERHEATED.serialize());
		}
	}

	RecipeSchema PROCESSING_DEFAULT = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT);

	RecipeSchema ITEM_APPLICATION = new RecipeSchema(ProcessingRecipeJS.class, ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT, KEEP_HELD_ITEM);
}
