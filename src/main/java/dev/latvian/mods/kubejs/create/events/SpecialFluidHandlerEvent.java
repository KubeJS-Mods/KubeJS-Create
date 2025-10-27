package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.api.registry.SimpleRegistry;
import dev.latvian.mods.kubejs.create.core.mixin.FluidIngredientStacksInvoker;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class SpecialFluidHandlerEvent implements KubeEvent {
	public interface PipeHandler {
		void apply(Level level, AABB aabb, FluidStack fluid);
	}

	public void add(FluidIngredient fluidIngredient, PipeHandler handler) {
		OpenPipeEffectHandler.REGISTRY.registerProvider(createEffectHandler(fluidIngredient, handler));
	}

	static SimpleRegistry.Provider<Fluid, OpenPipeEffectHandler> createEffectHandler(FluidIngredient fluidIngredient, SpecialFluidHandlerEvent.PipeHandler handler) {
		return new SimpleRegistry.Provider<>() {
			// hack to make it so people don't need to use Supplier<FluidIngredient> as a param, this sucks
			Set<Fluid> validFluids = null;

			final OpenPipeEffectHandler internalHandler = (level, aabb, fluid) -> {
				if (fluidIngredient.test(fluid)) {
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
					// this relies on the stacks returned by the ingredient also being
					// exactly all the fluids that are accepted, but that should be true
					// for the vast majority
					var stacks = ((FluidIngredientStacksInvoker) fluidIngredient).callGenerateStacks();

					validFluids = stacks.map(FluidStack::getFluid).collect(Collectors.toSet());
				}

				return validFluids;
			}

			@Override
			public void onRegister(Runnable invalidate) {
				NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> {
					if (event.shouldUpdateStaticData()) {
						invalidate.run();
						validFluids = null;
					}
				});
			}
		};
	}
}
