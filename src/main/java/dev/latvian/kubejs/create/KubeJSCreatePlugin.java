package dev.latvian.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.item.ItemBuilder;
import dev.latvian.kubejs.item.custom.ItemType;
import dev.latvian.kubejs.item.custom.ItemTypes;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {
	@Override
	public void init() {
		ItemTypes.register(new ItemType("create:sequenced_assembly") {
			@Override
			public Item createItem(ItemBuilder itemBuilder) {
				return new SequencedAssemblyItem(itemBuilder.createItemProperties());
			}
		});
	}

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation("create:sequenced_assembly"), SequencedAssemblyRecipeJS::new);
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (AllRecipeTypes type : AllRecipeTypes.values()) {
			if (type.getSerializer() instanceof ProcessingRecipeSerializer) {
				event.register(type.getSerializer().getRegistryName(), ProcessingRecipeJS::new);
			}
		}
	}
}