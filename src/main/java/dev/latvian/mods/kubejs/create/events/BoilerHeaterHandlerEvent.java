package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.boiler.BoilerHeater;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.BoilerHeaterHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHandlerEvent extends EventJS {
	@FunctionalInterface
	public interface BoilerHeaterCallback {
		float updateHeat(BlockContainerJS block);
	}

	public void add(Block block, BoilerHeaterCallback onUpdate) {
		BoilerHeaterHelper.registerHeaterPlatform(block, onUpdate);
	}

	public void addAdvanced(BlockStatePredicate block, BoilerHeaterCallback onUpdate) {
		BoilerHeater.REGISTRY.registerProvider(key -> {
			// Check if any blockstate of this block matches our predicate
			for (var state : key.getStateDefinition().getPossibleStates()) {
				if (block.test(state)) {
					// Return a BoilerHeater that calls our callback
					return (level, pos, blockState) -> onUpdate.updateHeat(level.kjs$getBlock(pos));
				}
			}
			return null;
		});
	}
}