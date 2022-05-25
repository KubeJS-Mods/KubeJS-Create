package dev.latvian.mods.kubejs.create;

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
	private final List<Pair<ResourceLocation, BlockSpoutingBehaviour>> behaviours;

	@FunctionalInterface
	public interface SpoutHandler {
		long fillBlock(BlockContainerJS block, FluidStackJS fluid, boolean simulate);
	}

	public SpecialSpoutHandlerEvent(List<Pair<ResourceLocation, BlockSpoutingBehaviour>> behaviours) {
		this.behaviours = behaviours;
	}

	public void addSpoutHandler(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		behaviours.add(Pair.of(path, FluidIngredientHelper.createSpoutingHandler(block, handler)));
	}
}
