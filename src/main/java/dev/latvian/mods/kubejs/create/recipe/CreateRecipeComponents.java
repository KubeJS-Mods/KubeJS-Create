package dev.latvian.mods.kubejs.create.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;

public interface CreateRecipeComponents {
	RecipeComponentType<HeatCondition> HEAT_CONDITION = EnumComponent.of(Create.asResource("heat_condition"), HeatCondition.class, HeatCondition.CODEC);
}
