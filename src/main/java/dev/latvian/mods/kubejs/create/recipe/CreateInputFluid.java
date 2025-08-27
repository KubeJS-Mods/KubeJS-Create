package dev.latvian.mods.kubejs.create.recipe;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.core.KubeFluidTagIngredient;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public record CreateInputFluid(FluidIngredient ingredient) implements FluidLike {
	public static final CreateInputFluid EMPTY = new CreateInputFluid(FluidIngredient.EMPTY);

	@Override
	public boolean kjs$isEmpty() {
		return ingredient.equals(FluidIngredient.EMPTY) || ingredient.getMatchingFluidStacks().isEmpty();
	}

	@Override
	public int kjs$getAmount() {
		return ingredient.getRequiredAmount();
	}

	@Override
	public Fluid kjs$getFluid() {
		var stacks = ingredient.getMatchingFluidStacks();
		return stacks.isEmpty() ? Fluids.EMPTY : stacks.getFirst().getFluid();
	}

	@Override
	public FluidLike kjs$copy(int amount) {
		if (ingredient instanceof FluidIngredient.FluidStackIngredient in) {
			var fs = in.getMatchingFluidStacks().getFirst();
			var fs1 = new FluidStack(fs.getFluidHolder(), amount, fs.getComponentsPatch());
			return (FluidLike) (Object) fs1;
		} else if (ingredient instanceof FluidIngredient.FluidTagIngredient in && in.getRequiredAmount() != amount && (Object) in instanceof KubeFluidTagIngredient kin) {
			return new CreateInputFluid(FluidIngredient.fromTag(kin.kjs$getTag(), amount));
		}

		return this;
	}
}
