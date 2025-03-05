package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FanProcessingEvent extends EventJS {
	private final List<FanProcessingTypeJS.Builder> builders = new ArrayList<>();

	public FanProcessingTypeJS.Builder create(ResourceLocation id) {
		FanProcessingTypeJS.Builder builder = new FanProcessingTypeJS.Builder(id);
		builders.add(builder);
		return builder;
	}

	@HideFromJS
	public void register() {
		for (FanProcessingTypeJS.Builder builder : builders) {
			FanProcessingTypeRegistry.register(builder.id, builder.build());
		}
	}
}
