package dev.latvian.mods.kubejs.create.recipe;

import com.mojang.serialization.Codec;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.match.FluidMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.kubejs.util.OpsContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class CreateFluidIngredientRecipeComponent implements RecipeComponent<FluidIngredient> {
	public static final TypeInfo TYPE_INFO = TypeInfo.of(FluidIngredient.class);
	public static final RecipeComponentType<FluidIngredient> TYPE = RecipeComponentType.unit(Create.asResource("fluid_ingredient"), new CreateFluidIngredientRecipeComponent());

	@Override
	public RecipeComponentType<?> type() {
		return TYPE;
	}

	@Override
	public Codec<FluidIngredient> codec() {
		return FluidIngredient.CODEC;
	}

	@Override
	public TypeInfo typeInfo() {
		return TYPE_INFO;
	}

	@Override
	public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
		return from instanceof SizedFluidIngredient || from instanceof FluidIngredient || from instanceof FluidStack || from instanceof Fluid;
	}

	@Override
	public boolean matches(Context cx, KubeRecipe recipe, FluidIngredient value, ReplacementMatchInfo match) {
		return match.match() instanceof FluidMatch m && m.matches(cx, FluidIngredientHelper.convert(value), match.exact());
	}

	@Override
	public boolean isEmpty(FluidIngredient value) {
		return value == FluidIngredient.EMPTY;
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, FluidIngredient value) {
		if (value != FluidIngredient.EMPTY) {
			var stacks = value.getMatchingFluidStacks();

			if (!stacks.isEmpty()) {
				builder.append(stacks.getFirst().getFluid().kjs$getIdLocation());
			}
		}
	}

	@Override
	public String toString() {
		return TYPE.toString();
	}

	@Override
	public String toString(OpsContainer ops, FluidIngredient value) {
		// Better toString?
		return value.toString();
	}
}
