package dev.latvian.mods.kubejs.create.platform.forge;

import com.simibubi.create.content.contraptions.fluids.tank.BoilerHeaters;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHelperImpl {
    public static void registerHeaterPlatform(Block block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
        BoilerHeaters.registerHeater(block, (level, blockPos, blockState) -> onUpdate.updateHeat(level.kjs$getBlock(blockPos)));
    }
}
