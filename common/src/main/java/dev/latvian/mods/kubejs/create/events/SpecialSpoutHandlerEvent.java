package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.foundation.utility.Pair;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * @author Prunoideae
 */
public class SpecialSpoutHandlerEvent extends EventJS {
	public static final String ID = "create.spout.special";

	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(BlockContainerJS block, FluidStackJS fluid, boolean simulate);
	}

	public void addSpoutHandler(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		BlockSpoutingBehaviour.addCustomSpoutInteraction(path, FluidIngredientHelper.createSpoutingHandler(block, handler));
	}
}
