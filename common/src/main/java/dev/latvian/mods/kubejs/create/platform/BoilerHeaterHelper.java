package dev.latvian.mods.kubejs.create.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHelper {
	@ExpectPlatform
	public static void registerHeaterPlatform(Block block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
		throw new AssertionError("Not implemented");
	}
}
