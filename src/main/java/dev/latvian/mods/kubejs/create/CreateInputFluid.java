package dev.latvian.mods.kubejs.create;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public record CreateInputFluid(FluidIngredient ingredient) implements InputFluid {
	public static final CreateInputFluid EMPTY = new CreateInputFluid(FluidIngredient.EMPTY);

	@Override
	public boolean kjs$isEmpty() {
		return ingredient.equals(FluidIngredient.EMPTY) || ingredient.getMatchingFluidStacks().isEmpty();
	}

	@Override
	public long kjs$getAmount() {
		return ingredient.getRequiredAmount();
	}

	@Override
	public FluidLike kjs$copy(long amount) {
		if (ingredient instanceof FluidIngredient.FluidStackIngredient in) {
			var fs = in.getMatchingFluidStacks().get(0);
			return FluidStackJS.of(fs.getFluid(), amount, fs.getTag() == null ? null : fs.getTag().copy());
		} else if (ingredient instanceof FluidIngredient.FluidTagIngredient in && in.getRequiredAmount() != amount) {
			var tag = TagKey.create(Registries.FLUID, new ResourceLocation(in.serialize().get("fluidTag").getAsString()));
			return new CreateInputFluid(FluidIngredient.fromTag(tag, (int) amount));
		}

		return this;
	}
}
