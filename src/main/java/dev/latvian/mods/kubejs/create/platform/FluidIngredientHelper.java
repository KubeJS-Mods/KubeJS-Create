package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.EmptyFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FluidIngredientHelper {
	public static FluidIngredient toFluidIngredient(FluidStack fluidStack) {
		return FluidIngredient.fromFluidStack(fluidStack);
	}

	public static FluidIngredient convert(net.neoforged.neoforge.fluids.crafting.FluidIngredient fluidIngredient) {
		return FluidIngredient.EMPTY;
	}

	public static net.neoforged.neoforge.fluids.crafting.FluidIngredient convert(FluidIngredient fluidIngredient) {
		return EmptyFluidIngredient.INSTANCE;
	}

	public static SimpleRegistry.Provider<Fluid, OpenPipeEffectHandler> createEffectHandler(FluidIngredient fluidIngredient, SpecialFluidHandlerEvent.PipeHandler handler) {
		return new SimpleRegistry.Provider<>() {
			final FluidIngredient filter = fluidIngredient;
			Set<Fluid> validFluids = null;

			final OpenPipeEffectHandler internalHandler = (level, aabb, fluid) -> {
				if (filter.test(fluid)) {
					handler.apply(level, aabb, fluid);
				}
			};

			@Override
			@Nullable
			public OpenPipeEffectHandler get(Fluid fluidIn) {
				if (getValidFluids().contains(fluidIn)) {
					return internalHandler;
				} else {
					return null;
				}
			}

			private Set<Fluid> getValidFluids() {
				if (validFluids == null) {
					var set = new HashSet<Fluid>();

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
				NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> {
					if (event.shouldUpdateStaticData()) {
						invalidate.run();
						filter.matchingFluidStacks = null;
						validFluids = null;
					}
				});
			}
		};
	}

	public static SimpleRegistry.Provider<Block, BlockSpoutingBehaviour> createSpoutingHandler(BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
		final BlockSpoutingBehaviour internalHandler = (world, pos, spout, availableFluid, simulate) -> {
			if (!block.test(world.getBlockState(pos))) {
				return 0;
			}

			return (int) handler.fillBlock(world.kjs$getBlock(pos), availableFluid, simulate);
		};

		return blockIn -> block.testBlock(blockIn) ? internalHandler : null;
	}

	public static FluidIngredient wrap(Context cx, Object o) {
		if (o instanceof FluidStack fluidStackJS) {
			return toFluidIngredient(fluidStackJS);
		} else if (o instanceof Map<?, ?> map && map.containsKey("type")) {
			return FluidIngredient.CODEC.parse(RegistryAccessContainer.of(cx).java(), map).getOrThrow();
		} else {
			return toFluidIngredient(FluidWrapper.wrap(RegistryAccessContainer.of(cx), o));
		}
	}
}
