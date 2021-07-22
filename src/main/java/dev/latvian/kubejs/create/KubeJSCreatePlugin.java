package dev.latvian.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {
	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.ignore(new ResourceLocation("create:sequenced_assembly"));
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (AllRecipeTypes type : AllRecipeTypes.values()) {
			if (type.serializer instanceof ProcessingRecipeSerializer) {
				event.register(type.serializer.getRegistryName(), ProcessingRecipeJS::new);
			}
		}
	}
}