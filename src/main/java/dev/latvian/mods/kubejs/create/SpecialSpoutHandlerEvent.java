package dev.latvian.mods.kubejs.create;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.contraptions.fluids.actors.SpoutTileEntity;
import com.simibubi.create.foundation.utility.Pair;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * @author Prunoideae
 */
public class SpecialSpoutHandlerEvent extends EventJS {
	public static final String ID = "create.spout.special";
	private final List<Pair<ResourceLocation, BlockSpoutingBehaviour>> behaviours;

	@FunctionalInterface
	public interface SpoutHandler {
		int fillBlock(BlockContainerJS block, FluidStackJS fluid, boolean simulate);
	}

	public SpecialSpoutHandlerEvent(List<Pair<ResourceLocation, BlockSpoutingBehaviour>> behaviours) {
		this.behaviours = behaviours;
	}

	public void addSpoutHandler(ResourceLocation path, BlockStatePredicate block, SpoutHandler handler) {
		behaviours.add(Pair.of(path, new BlockSpoutingBehaviour() {
			@Override
			public int fillBlock(Level world, BlockPos pos, SpoutTileEntity spout, FluidStack availableFluid, boolean simulate) {
				if (!block.test(world.getBlockState(pos)))
					return 0;
				return handler.fillBlock(new BlockContainerJS(world, pos), FluidStackJS.of(FluidStackHooksForge.fromForge(availableFluid)), simulate);
			}
		}));
	}
}
