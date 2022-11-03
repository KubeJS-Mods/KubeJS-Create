package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.create.cogwheel.CogWheelBlockBuilder;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialFluidHandlerEvent;
import dev.latvian.mods.kubejs.create.events.SpecialSpoutHandlerEvent;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
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
		RegistryObjectBuilderTypes.BLOCK.addType("create:cog_wheel", CogWheelBlockBuilder.class, CogWheelBlockBuilder::new);
	}

	@Override
	public void afterInit() {
		new SpecialSpoutHandlerEvent().post(ScriptType.STARTUP, SpecialSpoutHandlerEvent.ID);
		new SpecialFluidHandlerEvent().post(ScriptType.STARTUP, SpecialFluidHandlerEvent.ID);
		new BoilerHeaterHandlerEvent().post(ScriptType.STARTUP, BoilerHeaterHandlerEvent.ID);
	}

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation("create:sequenced_assembly"), SequencedAssemblyRecipeJS::new);
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (var createRecipeType : AllRecipeTypes.values()) {
			if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer) {
				event.register(createRecipeType.getId(), recipeProviders.getOrDefault(createRecipeType.getId(), ProcessingRecipeJS::new));
			}
		}
	}
}