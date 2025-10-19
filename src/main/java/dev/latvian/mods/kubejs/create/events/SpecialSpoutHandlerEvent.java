package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.wrapper.FluidIngredientWrapper;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.level.LevelBlock;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

public class SpecialSpoutHandlerEvent implements KubeEvent {
	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(LevelBlock block, FluidStack fluid, boolean simulate);
	}

	public void add(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		BlockSpoutingBehaviour.BY_BLOCK.registerProvider(FluidIngredientWrapper.createSpoutingHandler(block, handler));
	}
}
