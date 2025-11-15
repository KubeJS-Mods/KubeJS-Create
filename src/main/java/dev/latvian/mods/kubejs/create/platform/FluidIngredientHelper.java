package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class FluidIngredientHelper {
	public static FluidIngredient toFluidIngredient(FluidStackJS fluidStack) {
		return FluidIngredient.fromFluidStack(new FluidStack(FluidStackHooksFabric.toFabric(fluidStack.getFluidStack()),fluidStack.kjs$getAmount()));
	}

	public static SimpleRegistry.Provider<Fluid, OpenPipeEffectHandler> createEffectHandler(FluidIngredient fluidIngredient, SpecialFluidHandlerEvent.PipeHandler handler) {
		return new SimpleRegistry.Provider<>() {
			final FluidIngredient filter = fluidIngredient;
			Set<Fluid> validFluids = null;

			final OpenPipeEffectHandler internalHandler = (level, aabb, fluid) -> {
				if (filter.test(fluid)) {
					handler.apply(level, aabb, FluidStackJS.of(fluid));
				}
			};

			@Override
			public @Nullable OpenPipeEffectHandler get(Fluid fluidIn) {
				if (getValidFluids().contains(fluidIn)) {
					return internalHandler;
				} else {
					return null;
				}
			}

			private Set<Fluid> getValidFluids() {
				if (validFluids == null) {
					Set<Fluid> set = new HashSet<>();
					for (FluidStack fluidStack : fluidIngredient.getMatchingFluidStacks()) {
						Fluid fluid = fluidStack.getFluid();
						set.add(fluid);
					}
					validFluids = set;
				}
				return validFluids;
			}

			@Override
			public void onRegister(Runnable invalidate) {
				// Fabric: Tag update handling could be added here if needed
				// For now, the cache will be built once and remain valid
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
