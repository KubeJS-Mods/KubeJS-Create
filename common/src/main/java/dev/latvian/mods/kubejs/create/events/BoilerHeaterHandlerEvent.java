package dev.latvian.mods.kubejs.create.events;

import com.simibubi.create.content.contraptions.fluids.tank.BoilerHeaters;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.create.platform.BoilerHeaterHelper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.level.block.Block;

public class BoilerHeaterHandlerEvent extends EventJS {
    public static final String ID = "boilerHeatHandler";

    @FunctionalInterface
    public interface BoilerHeaterCallback {
        float updateHeat(BlockContainerJS block);
    }

    public void registerHeater(Block block, BoilerHeaterCallback onUpdate) {
        BoilerHeaterHelper.registerHeaterPlatform(block, onUpdate);
    }

    public void registerHeaterAdvanced(BlockStatePredicate block, BoilerHeaterCallback onUpdate) {
        BoilerHeaters.registerHeaterProvider(((level, blockPos, blockState) -> block.test(blockState) ? (l, b, bs) -> onUpdate.updateHeat(l.kjs$getBlock(b)) : null));
    }
}