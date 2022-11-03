package dev.latvian.mods.kubejs.create.platform;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.latvian.mods.kubejs.create.events.BoilerHeaterHandlerEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CogWheelHelper {

	@ExpectPlatform
	public static void registerModel(Block block) {
		throw new AssertionError("Not implemented");
	}

	@ExpectPlatform
	public static BlockEntityType<BracketedKineticTileEntity> getCogWheel() {
		throw new AssertionError("Not implemented");
	}

}
