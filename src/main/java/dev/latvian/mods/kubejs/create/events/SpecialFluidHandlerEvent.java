package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.contraptions.fluids.OpenEndedPipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.MapJS;

import java.util.function.BiConsumer;

/**
 * @author Prunoideae
 */
public class SpecialFluidHandlerEvent extends EventJS {
	public static final String ID = "create.pipe.fluid_effect";

	public void addFluidHandler(Object fluidStack, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
		FluidIngredient fluidIngredient;
		if (fluidStack instanceof FluidStackJS fluidStackJS) {
			fluidIngredient = FluidIngredientHelper.toFluidIngredient(fluidStackJS);
		} else if (fluidStack instanceof MapJS map && (map.containsKey("fluid") || map.containsKey("fluidTag"))) {
			fluidIngredient = FluidIngredient.deserialize(map.toJson());
		} else {
			fluidIngredient = FluidIngredientHelper.toFluidIngredient(FluidStackJS.of(fluidStack));
		}

		OpenEndedPipe.registerEffectHandler(FluidIngredientHelper.createEffectHandler(fluidIngredient, handler));
	}
}
