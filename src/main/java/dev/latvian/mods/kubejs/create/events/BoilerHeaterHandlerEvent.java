package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.boiler.BoilerHeater;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHandlerEvent extends EventJS {
	@FunctionalInterface
	public interface BoilerHeaterCallback {
		float updateHeat(BlockContainerJS block);
	}

	public void add(Block block, BoilerHeaterCallback onUpdate) {
		BoilerHeater.REGISTRY.register(block, (level, blockPos, blockState) -> onUpdate.updateHeat(level.kjs$getBlock(blockPos)));
	}

	public void addAdvanced(BlockStatePredicate block, BoilerHeaterCallback onUpdate) {
		final BoilerHeater internalHandler = (level, blockPos, blockState) -> block.test(blockState)
				? onUpdate.updateHeat(level.kjs$getBlock(blockPos))
				: BoilerHeater.NO_HEAT;

		BoilerHeater.REGISTRY.registerProvider((blockIn) -> block.testBlock(blockIn) ? internalHandler : null);
	}
}