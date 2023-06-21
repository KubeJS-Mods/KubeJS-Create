package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

public class FluidIngredientHelper {
	public static FluidIngredient toFluidIngredient(FluidStackJS fluidStack) {
		return FluidIngredient.fromFluidStack(FluidStack.loadFluidStackFromNBT(fluidStack.toNBT()));
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
			public long fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
				if (!block.test(world.getBlockState(pos))) {
					return 0;
				}
				return handler.fillBlock(new BlockContainerJS(world, pos), FluidStackJS.of(availableFluid.writeToNBT(new CompoundTag())), simulate);
			}
		};
	}
}
