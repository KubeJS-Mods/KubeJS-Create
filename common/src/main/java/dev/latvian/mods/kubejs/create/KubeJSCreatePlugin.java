package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {
	@Override
	public void init() {
		RegistryObjectBuilderTypes.ITEM.addType("create:sequenced_assembly", SequencedAssemblyItemBuilder.class, SequencedAssemblyItemBuilder::new);
	}

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation("create:sequenced_assembly"), SequencedAssemblyRecipeJS::new);
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (var createRecipeType : AllRecipeTypes.values()) {
			if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer) {
				event.register(createRecipeType.getId(), ProcessingRecipeJS::new);
			}
		}
	}
}