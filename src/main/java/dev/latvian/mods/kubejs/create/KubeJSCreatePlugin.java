package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Lang;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.create.events.CreateEvents;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

import java.util.Map;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {

	private static final Map<AllRecipeTypes, RecipeSchema> recipeSchemas = Map.of(
			AllRecipeTypes.DEPLOYING, ProcessingRecipeSchema.ITEM_APPLICATION,
			AllRecipeTypes.ITEM_APPLICATION, ProcessingRecipeSchema.ITEM_APPLICATION
	);

	@Override
	public void init() {
		RegistryInfo.ITEM.addType("create:sequenced_assembly", SequencedAssemblyItemBuilder.class, SequencedAssemblyItemBuilder::new);
	}

	@Override
	public void registerEvents() {
		CreateEvents.GROUP.register();
	}

	@Override
	public void afterInit() {
		CreateEvents.BOILER_HEATER.post(ScriptType.STARTUP, new BoilerHeaterHandlerEvent());
		CreateEvents.SPECIAL_FLUID.post(ScriptType.STARTUP, new SpecialFluidHandlerEvent());
		CreateEvents.SPECIAL_SPOUT.post(ScriptType.STARTUP, new SpecialSpoutHandlerEvent());
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
	public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
		var ns = event.namespace("create");

		ns.shaped("mechanical_crafting")
				.register("sequenced_assembly", SequencedAssemblyRecipeSchema.SCHEMA);

		for (var createRecipeType : AllRecipeTypes.values()) {
			if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
				var schema = recipeSchemas.getOrDefault(createRecipeType, ProcessingRecipeSchema.PROCESSING_DEFAULT);
				ns.register(Lang.asId(createRecipeType.name()), schema);
			}
		}
	}
}