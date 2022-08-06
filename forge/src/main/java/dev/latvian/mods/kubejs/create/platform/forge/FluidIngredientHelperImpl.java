package dev.latvian.mods.kubejs.create.platform.forge;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.contraptions.fluids.OpenEndedPipe;
import com.simibubi.create.content.contraptions.fluids.actors.SpoutTileEntity;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.BiConsumer;

public class FluidIngredientHelperImpl {
	public static FluidIngredient toFluidIngredient(FluidStackJS fluidStack) {
		return FluidIngredient.fromFluidStack(FluidStackHooksForge.toForge(fluidStack.getFluidStack()));
	}

	public static OpenEndedPipe.IEffectHandler createEffectHandler(FluidIngredient fluidIngredient, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
		return new OpenEndedPipe.IEffectHandler() {
			@Override
			public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
				return fluidIngredient.test(fluid);
			}

			@Override
			public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
				handler.accept(pipe, FluidStackJS.of(fluid));
			}
		};
	}

	public static BlockSpoutingBehaviour createSpoutingHandler(BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
		return new BlockSpoutingBehaviour() {
			@Override
			public int fillBlock(Level world, BlockPos pos, SpoutTileEntity spout, FluidStack availableFluid, boolean simulate) {
				if (!block.test(world.getBlockState(pos)))
					return 0;
				return (int) handler.fillBlock(new BlockContainerJS(world, pos), FluidStackJS.of(FluidStackHooksForge.fromForge(availableFluid)), simulate);
			}
		};
	}
}
