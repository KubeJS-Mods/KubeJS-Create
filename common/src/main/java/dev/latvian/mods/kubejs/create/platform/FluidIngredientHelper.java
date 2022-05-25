package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.contraptions.fluids.OpenEndedPipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;

import java.util.function.BiConsumer;

import static dev.latvian.mods.kubejs.create.SpecialSpoutHandlerEvent.SpoutHandler;

public class FluidIngredientHelper {

	@ExpectPlatform
	public static FluidIngredient toFluidIngredient(FluidStackJS fluidStack) {
		throw new AssertionError("Not implemented");
	}

	@ExpectPlatform

	public static OpenEndedPipe.IEffectHandler createEffectHandler(FluidIngredient fluidIngredient, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
		throw new AssertionError("Not implemented");
	}

	@ExpectPlatform

	public static BlockSpoutingBehaviour createSpoutingHandler(BlockStatePredicate block, SpoutHandler handler) {
		throw new AssertionError("Not implemented");
	}
}
