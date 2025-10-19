package dev.latvian.mods.kubejs.create.platform;

import com.mojang.serialization.DataResult;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.EmptyFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mojang.serialization.DataResult.error;
import static com.mojang.serialization.DataResult.success;

public class FluidIngredientHelper {
	public static FluidIngredient toFluidIngredient(FluidStack fluidStack) {
		return FluidIngredient.fromFluidStack(fluidStack);
	}

	public static FluidIngredient toFluidIngredient(TagKey<Fluid> tag) {
		return FluidIngredient.fromTag(tag, FluidType.BUCKET_VOLUME);
	}

	public static FluidIngredient toFluidIngredient(Fluid fluid) {
		return FluidIngredient.fromFluid(fluid, FluidType.BUCKET_VOLUME);
	}

	public static DataResult<FluidIngredient> convert(SizedFluidIngredient neoIn) {
		return switch (neoIn.ingredient()) {
			case SingleFluidIngredient single -> success(FluidIngredient.fromFluid(single.fluid().value(), neoIn.amount()));
			case TagFluidIngredient tag -> success(FluidIngredient.fromTag(tag.tag(), neoIn.amount()));
			case DataComponentFluidIngredient nbt -> switch (nbt.fluids()) {
				case HolderSet.Direct<Fluid> __ when nbt.fluids().size() == 1 -> {
					var fluid = nbt.fluids().get(0);
					var stack = new FluidStack(fluid, neoIn.amount(), nbt.components().asPatch());

					yield success(FluidIngredient.fromFluidStack(stack));
				}
				default -> error(() -> "Create FluidIngredients only support single fluids when using components!");

			};
			case EmptyFluidIngredient empty -> success(FluidIngredient.EMPTY);
			default -> error(() -> "Create FluidIngredients only support single fluid, fluid with data, or tag ingredients!");
		};
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
		return (switch (o) {
			case FluidIngredient id -> success(id);
			case FluidStack stack -> success(toFluidIngredient(stack));
			case TagKey<?> tag -> success(toFluidIngredient(FluidTags.create(tag.location())));
			case Fluid fluid -> success(toFluidIngredient(fluid));
			case SizedFluidIngredient neoIn -> convert(neoIn);
			case net.neoforged.neoforge.fluids.crafting.FluidIngredient neoIn -> convert(new SizedFluidIngredient(neoIn, FluidType.BUCKET_VOLUME));
			case Map<?, ?> map when map.containsKey("type") -> FluidIngredient.CODEC.parse(RegistryAccessContainer.of(cx).java(), map);
			default -> convert(FluidWrapper.wrapSizedIngredient(cx, o));
		}).getOrThrow(msg -> new KubeRuntimeException("Failed to parse Create FluidIngredient: " + msg).source(SourceLine.of(cx)));
	}
}
