package dev.latvian.mods.kubejs.create.core.mixin;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(FluidIngredient.class)
public interface FluidIngredientStacksInvoker {
	@Invoker
	Stream<FluidStack> callGenerateStacks();
}
