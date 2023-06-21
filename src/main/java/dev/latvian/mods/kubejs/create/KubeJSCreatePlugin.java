package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.create.events.CreateEvents;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {

	private static final Map<ResourceLocation, Supplier<RecipeJS>> recipeProviders = Map.of(
			new ResourceLocation("create:deploying"), ItemApplicationRecipeJS::new,
			new ResourceLocation("create:item_application"), ItemApplicationRecipeJS::new
	);

	@Override
	public void init() {
		RegistryObjectBuilderTypes.ITEM.addType("create:sequenced_assembly", SequencedAssemblyItemBuilder.class, SequencedAssemblyItemBuilder::new);
	}

	@Override
	public void registerEvents() {
		CreateEvents.GROUP.register();
	}

	@Override
	public void afterInit() {
		CreateEvents.BOILER_HEATER.post(new BoilerHeaterHandlerEvent());
		CreateEvents.SPECIAL_FLUID.post(new SpecialFluidHandlerEvent());
		CreateEvents.SPECIAL_SPOUT.post(new SpecialSpoutHandlerEvent());
	}

	@Override
	public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
		typeWrappers.register(FluidIngredient.class, this::wrapFluidIngredient);
	}

	private FluidIngredient wrapFluidIngredient(Context cx, Object o) {
		if (o instanceof FluidStackJS fluidStackJS) {
			return FluidIngredientHelper.toFluidIngredient(fluidStackJS);
		} else if (o instanceof Map<?, ?> map && (map.containsKey("fluid") || map.containsKey("fluidTag"))) {
			return FluidIngredient.deserialize(MapJS.json(map));
		} else {
			return FluidIngredientHelper.toFluidIngredient(FluidStackJS.of(o));
		}
	}

	@Override
	public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
		event.register(new ResourceLocation("create:sequenced_assembly"), SequencedAssemblyRecipeJS::new);
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (var createRecipeType : AllRecipeTypes.values()) {
			if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
				event.register(createRecipeType.getId(), recipeProviders.getOrDefault(createRecipeType.getId(), ProcessingRecipeJS::new));
			}
		}
	}
}