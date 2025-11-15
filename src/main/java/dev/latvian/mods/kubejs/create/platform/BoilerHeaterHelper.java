package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.api.boiler.BoilerHeater;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHelper {
    public static void registerHeaterPlatform(Block block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
        BoilerHeater.REGISTRY.register(block, (level, blockPos, blockState) -> onUpdate.updateHeat(level.kjs$getBlock(blockPos)));
    }
}
