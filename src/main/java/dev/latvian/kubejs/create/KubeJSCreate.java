package dev.latvian.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Lang;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.kubejs.recipe.minecraft.ShapedRecipeJS;
import net.minecraft.util.ResourceLocation;
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
		event.ignore(new ResourceLocation("create:blockzapper_upgrade"));
		event.register(new ResourceLocation("create:mechanical_crafting"), ShapedRecipeJS::new);

		for (AllRecipeTypes type : AllRecipeTypes.values())
		{
			if (type.supplier.get() instanceof ProcessingRecipeSerializer)
			{
				event.register(new ResourceLocation("create", Lang.asId(type.name())), ProcessingRecipeJS::new);
			}
		}
	}
}