package dev.latvian.mods.kubejs.create.core.mixin;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.core.KubeFluidTagIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FluidIngredient.FluidTagIngredient.class)
public abstract class FluidTagIngredientMixin implements KubeFluidTagIngredient {
	@Shadow
	private TagKey<Fluid> tag;

	@Override
	public TagKey<Fluid> kjs$getTag() {
		return tag;
	}
}
