package dev.latvian.mods.kubejs.create.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.codec.CreateCodecs;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.SizedFluidIngredientComponent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public interface CreateRecipeComponents {
	RecipeComponentType<HeatCondition> HEAT_CONDITION = EnumComponent.of(Create.asResource("heat_condition"), HeatCondition.class, HeatCondition.CODEC);

	// uses create's custom codec for sized ingredients that always serialises the type for... reasons?
	RecipeComponentType<SizedFluidIngredient> SIZED_FLUID_INGREDIENT = RecipeComponentType.unit(
		Create.asResource("sized_fluid_ingredient"), (type) ->
			new SizedFluidIngredientComponent(type, CreateCodecs.FLAT_SIZED_FLUID_INGREDIENT_WITH_TYPE, false));
}
