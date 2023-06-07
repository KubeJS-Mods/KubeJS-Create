package dev.latvian.mods.kubejs.create;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.InputFluid;

public record CreateInputFluid(FluidIngredient ingredient) implements InputFluid {
	public static final CreateInputFluid EMPTY = new CreateInputFluid(FluidIngredient.EMPTY);

	@Override
	public boolean isInputEmpty() {
		return ingredient.equals(FluidIngredient.EMPTY) || ingredient.getMatchingFluidStacks().isEmpty();
	}
}
