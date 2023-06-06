package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Prunoideae
 */
public class SpecialSpoutHandlerEvent extends EventJS {
	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(BlockContainerJS block, FluidStackJS fluid, boolean simulate);
	}

	public void add(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		BlockSpoutingBehaviour.addCustomSpoutInteraction(path, FluidIngredientHelper.createSpoutingHandler(block, handler));
	}
}
