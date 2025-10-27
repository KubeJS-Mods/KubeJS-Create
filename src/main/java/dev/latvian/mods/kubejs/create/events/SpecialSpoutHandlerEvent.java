package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.level.LevelBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;

public class SpecialSpoutHandlerEvent implements KubeEvent {
	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(LevelBlock block, FluidStack fluid, boolean simulate);
	}

	public void add(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		BlockSpoutingBehaviour.BY_BLOCK.registerProvider(createSpoutingHandler(block, handler));
	}

	static SimpleRegistry.Provider<Block, BlockSpoutingBehaviour> createSpoutingHandler(BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
		final BlockSpoutingBehaviour internalHandler = (world, pos, spout, availableFluid, simulate) -> {
			if (!block.test(world.getBlockState(pos))) {
				return 0;
			}

			return (int) handler.fillBlock(world.kjs$getBlock(pos), availableFluid, simulate);
		};

		return blockIn -> block.testBlock(blockIn) ? internalHandler : null;
	}
}
