package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * @author Prunoideae
 */
public class SpecialFluidHandlerEvent extends EventJS {
	public interface PipeHandler {
		void apply(Level level, AABB aabb, FluidStackJS fluid);
	}

	public void add(FluidIngredient fluidIngredient, PipeHandler handler) {
		OpenPipeEffectHandler.REGISTRY.registerProvider(FluidIngredientHelper.createEffectHandler(fluidIngredient, handler));
	}
}
