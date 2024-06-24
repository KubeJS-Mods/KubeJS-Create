package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.utility.VecHelper;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class FanProcessingTypeJS implements FanProcessingType {
	private final BiPredicate<Level, BlockPos> isValidAt;
	private final int priority;
	private final BiPredicate<ItemStack, Level> canProcess;
	private final BiFunction<ItemStack, Level, List<ItemStack>> process;
	private final BiConsumer<Level, Vec3> spawnProcessingParticles;
	private final BiConsumer<AirFlowParticleAccess, RandomSource> morphAirFlow;
	private final BiConsumer<Entity, Level> affectEntity;


	private FanProcessingTypeJS(BiPredicate<Level, BlockPos> isValidAt, int priority, BiPredicate<ItemStack, Level> canProcess, BiFunction<ItemStack, Level, List<ItemStack>> process, BiConsumer<Level, Vec3> spawnProcessingParticles, BiConsumer<AirFlowParticleAccess, RandomSource> morphAirFlow, BiConsumer<Entity, Level> affectEntity) {

		this.isValidAt = isValidAt;
		this.priority = priority;
		this.canProcess = canProcess;
		this.process = process;
		this.spawnProcessingParticles = spawnProcessingParticles;
		this.morphAirFlow = morphAirFlow;
		this.affectEntity = affectEntity;
	}

	@Override
	public boolean isValidAt(Level level, BlockPos pos) {
		return isValidAt.test(level, pos);
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean canProcess(ItemStack stack, Level level) {
		return canProcess.test(stack, level);
	}

	@Override
	public @Nullable List<ItemStack> process(ItemStack stack, Level level) {
		List<ItemStack> result = process.apply(stack, level);
		return result == null ? null : new ArrayList<>(result);
	}

	@Override
	public void spawnProcessingParticles(Level level, Vec3 pos) {
		if (spawnProcessingParticles != null) {
			spawnProcessingParticles.accept(level, pos);
		}
	}

	@Override
	public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
		if (morphAirFlow != null) {
			morphAirFlow.accept(particleAccess, random);
		}
	}

	@Override
	public void affectEntity(Entity entity, Level level) {
		if (affectEntity != null) {
			affectEntity.accept(entity, level);
		}
	}

	public static class Builder {
		final ResourceLocation id;
		private BiPredicate<Level, BlockPos> isValidAt = (l, p) -> false;
		private int priority = 0;
		private BiPredicate<ItemStack, Level> canProcess = (i, l) -> false;
		private BiFunction<ItemStack, Level, List<ItemStack>> process = (i, l) -> null;
		private BiConsumer<Level, Vec3> spawnProcessingParticles = null;
		private BiConsumer<AirFlowParticleAccess, RandomSource> morphAirFlow = null;
		private BiConsumer<Entity, Level> affectEntity = null;

		@Info("Test if the block placed at the output of the fan is valid for this fan type.")
		public Builder isValidAt(BlockStatePredicate predicate) {
			return isValidAt((level, blockPos) -> predicate.test(level.getBlockState(blockPos)));
		}

		public Builder isValidAt(BiPredicate<Level, BlockPos> isValidAt) {
			this.isValidAt = isValidAt;
			return this;
		}

		@Info("Priority determines which fan type will be used if there are multiple available.")
		public Builder priority(int priority) {
			this.priority = priority;
			return this;
		}

		@Info("Test if the item can be processed by the fan type.")
		public Builder canProcess(Ingredient ingredient) {
			return canProcess(((itemStack, level) -> ingredient.test(itemStack)));
		}

		public Builder canProcess(BiPredicate<ItemStack, Level> canProcess) {
			this.canProcess = canProcess;
			return this;
		}

		@Info("Processes the item, return a list of item stacks as the result.")
		public Builder process(BiFunction<ItemStack, Level, List<ItemStack>> process) {
			this.process = process;
			return this;
		}

		@Info("Adds extra particles on items that are being processed.")
		public Builder spawnProcessingParticles(float frequency, ParticleOptions particleType) {
			return this.spawnProcessingParticles((level, vec3) -> {
				if (level.random.nextFloat() > 1 / (frequency + 1)) {
					return;
				}
				vec3 = vec3.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
						.multiply(1, 0.05f, 1)
						.normalize()
						.scale(0.15f));
				level.addParticle(particleType, vec3.x, vec3.y + .45f, vec3.z, 0, 0, 0);
			});
		}

		public Builder spawnProcessingParticles(BiConsumer<Level, Vec3> spawnProcessingParticles) {
			this.spawnProcessingParticles = spawnProcessingParticles;
			return this;
		}

		@Info("Modify the airflow emitted by the fan, changing its color, transparency or particles spawned.")
		public Builder morphAirFlow(Color color, float alpha, ParticleOptions particleOptions, float particleFrequency) {
			return morphAirFlow((airFlowParticleAccess, randomSource) -> {
				airFlowParticleAccess.setColor(color.getRgbJS());
				airFlowParticleAccess.setAlpha(alpha);
				if (particleOptions != null && randomSource.nextFloat() > 1 / (particleFrequency + 1)) {
					airFlowParticleAccess.spawnExtraParticle(particleOptions, .125f);
				}
			});
		}

		public Builder morphAirFlow(BiConsumer<AirFlowParticleAccess, RandomSource> morphAirFlow) {
			this.morphAirFlow = morphAirFlow;
			return this;
		}

		@Info("Defines how the entity will be affected by the airflow.")
		public Builder affectEntity(BiConsumer<Entity, Level> affectEntity) {
			this.affectEntity = affectEntity;
			return this;
		}

		public Builder(ResourceLocation id) {
			this.id = id;
		}

		@HideFromJS
		public FanProcessingTypeJS build() {
			return new FanProcessingTypeJS(
					isValidAt,
					priority,
					canProcess,
					process,
					spawnProcessingParticles,
					morphAirFlow,
					affectEntity
			);
		}
	}
}
