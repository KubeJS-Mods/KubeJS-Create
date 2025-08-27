package dev.latvian.mods.kubejs.create.core;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public interface KubeFluidTagIngredient {
	TagKey<Fluid> kjs$getTag();
}
