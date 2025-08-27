package dev.latvian.mods.kubejs.create;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.create.events.CreateEvents;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.create.item.SandpaperItemBuilder;
import dev.latvian.mods.kubejs.create.item.SequencedAssemblyItemBuilder;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.create.recipe.CreateFluidIngredientRecipeComponent;
import dev.latvian.mods.kubejs.create.recipe.CreateRecipeComponents;
import dev.latvian.mods.kubejs.create.recipe.ProcessingOutputRecipeComponent;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.minecraft.core.registries.Registries;

public class KubeJSCreatePlugin implements KubeJSPlugin {
	@Override
	public void afterInit() {
		CreateEvents.BOILER_HEATER.post(ScriptType.STARTUP, new BoilerHeaterHandlerEvent());
		CreateEvents.SPECIAL_FLUID.post(ScriptType.STARTUP, new SpecialFluidHandlerEvent());
		CreateEvents.SPECIAL_SPOUT.post(ScriptType.STARTUP, new SpecialSpoutHandlerEvent());
	}

	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(Registries.ITEM, callback -> {
			callback.add(Create.asResource("sequenced_assembly"), SequencedAssemblyItemBuilder.class, SequencedAssemblyItemBuilder::new);
			callback.add(Create.asResource("sandpaper"), SandpaperItemBuilder.class, SandpaperItemBuilder::new);
		});
	}

	@Override
	public void registerEvents(EventGroupRegistry registry) {
		registry.register(CreateEvents.GROUP);
	}

	@Override
	public void registerBindings(BindingRegistry bindings) {
		bindings.add("CreateProcessingOutput", ProcessingOutput.class);
	}

	@Override
	public void registerTypeWrappers(TypeWrapperRegistry registry) {
		registry.register(FluidIngredient.class, FluidIngredientHelper::wrap);
	}

	@Override
	public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
		// conversion
		// crushing (with time)
		// cutting (with time)
		// milling (with time)
		// basin
		// mixing (unwrapped)
		// compacting (unwrapped)
		// pressing
		// sandpaper_polishing
		// splashing
		// haunting
		// filling
		// emptying
	}

	@Override
	public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
		registry.register(ProcessingOutputRecipeComponent.TYPE);
		registry.register(CreateFluidIngredientRecipeComponent.TYPE);
		registry.register(CreateRecipeComponents.HEAT_CONDITION);
	}
}