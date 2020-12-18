package dev.latvian.kubejs.create;

import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.kubejs.recipe.minecraft.ShapedRecipeJS;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod(KubeJSCreate.MOD_ID)
@Mod.EventBusSubscriber(modid = KubeJSCreate.MOD_ID)
public class KubeJSCreate
{
	public static final String MOD_ID = "kubejs_create";

	@SubscribeEvent
	public static void registerRecipeHandlers(RegisterRecipeHandlersEvent event)
	{
		event.ignore("create:blockzapper_upgrade");
		event.register("create:mechanical_crafting", ShapedRecipeJS::new);
		event.register("create:conversion", ProcessingRecipeJS::new);
		event.register("create:crushing", ProcessingRecipeJS::new);
		event.register("create:cutting", ProcessingRecipeJS::new);
		event.register("create:milling", ProcessingRecipeJS::new);
		event.register("create:basin", ProcessingRecipeJS::new);
		event.register("create:mixing", ProcessingRecipeJS::new);
		event.register("create:compacting", ProcessingRecipeJS::new);
		event.register("create:pressing", ProcessingRecipeJS::new);
		event.register("create:sandpaper_polishing", ProcessingRecipeJS::new);
		event.register("create:splashing", ProcessingRecipeJS::new);
		event.register("create:filling", ProcessingRecipeJS::new);
		event.register("create:emptying", ProcessingRecipeJS::new);
	}
}