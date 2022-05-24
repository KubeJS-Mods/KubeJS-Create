package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.contraptions.fluids.OpenEndedPipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Pair;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class KubeJSCreatePlugin extends KubeJSPlugin {
	@Override
	public void init() {
		RegistryObjectBuilderTypes.ITEM.addType("create:sequenced_assembly", SequencedAssemblyItemBuilder.class, SequencedAssemblyItemBuilder::new);
	}

	@Override
	public void afterInit() {
		List<OpenEndedPipe.IEffectHandler> effects = new ArrayList<>();
		new SpecialFluidHandlerEvent(effects).post(ScriptType.STARTUP, SpecialFluidHandlerEvent.ID);
		effects.forEach(OpenEndedPipe::registerEffectHandler);

		List<Pair<ResourceLocation, BlockSpoutingBehaviour>> spouts = new ArrayList<>();
		new SpecialSpoutHandlerEvent(spouts).post(ScriptType.STARTUP, SpecialSpoutHandlerEvent.ID);
		spouts.forEach(s -> BlockSpoutingBehaviour.addCustomSpoutInteraction(s.getFirst(), s.getSecond()));
	}

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation("create:sequenced_assembly"), SequencedAssemblyRecipeJS::new);
		event.registerShaped(new ResourceLocation("create:mechanical_crafting"));

		for (var createRecipeType : AllRecipeTypes.values()) {
			if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer) {
				event.register(createRecipeType.getSerializer().getRegistryName(), ProcessingRecipeJS::new);
			}
		}
	}
}