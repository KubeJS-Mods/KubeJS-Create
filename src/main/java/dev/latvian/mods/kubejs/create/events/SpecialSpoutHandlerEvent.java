package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Prunoideae
 */
public class SpecialSpoutHandlerEvent extends EventJS {
	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(BlockContainerJS block, FluidStackJS fluid, boolean simulate);
	}

	public void add(BlockStatePredicate block, SpoutHandler handler) {
		BlockSpoutingBehaviour.BY_BLOCK.registerProvider(new SimpleRegistry.Provider<>() {
			@Override
			public BlockSpoutingBehaviour get(net.minecraft.world.level.block.Block key) {
				// Check if any block state of this block matches the predicate
				for (BlockState state : key.getStateDefinition().getPossibleStates()) {
					if (block.test(state)) {
						return FluidIngredientHelper.createSpoutingHandler(block, handler);
					}
				}
				return null;
			}
		});
	}
}
