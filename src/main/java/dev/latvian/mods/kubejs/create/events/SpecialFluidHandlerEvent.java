package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.wrapper.FluidIngredientWrapper;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class SpecialFluidHandlerEvent implements KubeEvent {
	public interface PipeHandler {
		void apply(Level level, AABB aabb, FluidStack fluid);
	}

	public void add(FluidIngredient fluidIngredient, PipeHandler handler) {
		OpenPipeEffectHandler.REGISTRY.registerProvider(FluidIngredientWrapper.createEffectHandler(fluidIngredient, handler));
	}
}
