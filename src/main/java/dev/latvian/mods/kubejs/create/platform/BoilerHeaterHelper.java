package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.content.contraptions.fluids.tank.BoilerHeaters;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHelper {
    public static void registerHeaterPlatform(Block block, BoilerHeaterHandlerEvent.BoilerHeaterCallback onUpdate) {
        BoilerHeaters.registerHeater(block, (level, blockPos, blockState) -> onUpdate.updateHeat(UtilsJS.getLevel(level).getBlock(blockPos)));
    }
}
