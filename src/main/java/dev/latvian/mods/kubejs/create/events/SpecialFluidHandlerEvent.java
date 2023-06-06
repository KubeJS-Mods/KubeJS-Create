package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;

import java.util.function.BiConsumer;

/**
 * @author Prunoideae
 */
public class SpecialFluidHandlerEvent extends EventJS {
	public void add(FluidIngredient fluidIngredient, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
		OpenEndedPipe.registerEffectHandler(FluidIngredientHelper.createEffectHandler(fluidIngredient, handler));
	}
}
