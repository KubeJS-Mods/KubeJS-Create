package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.create.events.CreateEvents;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.create.item.SandpaperItemBuilder;
import dev.latvian.mods.kubejs.create.item.SequencedAssemblyItemBuilder;
import dev.latvian.mods.kubejs.create.recipe.CreateRecipeComponents;
import dev.latvian.mods.kubejs.create.recipe.ProcessingOutputRecipeComponent;
import dev.latvian.mods.kubejs.create.wrapper.KubeCreateOutput;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.DataComponentTypeInfoRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

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
		bindings.add("CreateItem", KubeCreateOutput.class);
		// this implements KubeCreateOutput through a (mildly cursed) mixin
		bindings.add("CreateProcessingOutput", ProcessingOutput.class);
	}

	@Override
	public void registerTypeWrappers(TypeWrapperRegistry registry) {
		registry.register(ProcessingOutput.class, KubeCreateOutput::wrapProcessingOutput);
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
		registry.register(CreateRecipeComponents.SIZED_FLUID_INGREDIENT);
		registry.register(CreateRecipeComponents.HEAT_CONDITION);
	}

	@Override
	public void registerDataComponentTypeDescriptions(DataComponentTypeInfoRegistry registry) {
		try {
			for (var field : AllDataComponents.class.getDeclaredFields()) {
				if (field.getType() == DataComponentType.class
					&& Modifier.isPublic(field.getModifiers())
					&& Modifier.isStatic(field.getModifiers())
					&& field.getGenericType() instanceof ParameterizedType t
				) {
					var key = (DataComponentType) field.get(null);
					var typeInfo = TypeInfo.of(t.getActualTypeArguments()[0]);
					registry.register(key, typeInfo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}