package dev.latvian.mods.kubejs.create.platform.fabric;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileEntity;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.create.cogwheel.CustomCogwheelBlock;
import dev.latvian.mods.kubejs.create.fabric.CustomCogInstance;
import dev.latvian.mods.kubejs.create.fabric.KubeJSCreateFabric;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;

public class CogWheelHelperImpl {

    private static BlockEntityEntry<BracketedKineticTileEntity> customCogwheel;

    public static void registerModel(Block block) {
        CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new).accept(block);
    }

    public static BlockEntityType<BracketedKineticTileEntity> getCogWheel() {
        return customCogwheel.get();
    }

    public static void register() {
        var customBlocks = RegistryObjectBuilderTypes.BLOCK.deferredRegister.getRegistrar().entrySet();
        var customCogs = customBlocks.stream().map(Map.Entry::getValue).filter(CustomCogwheelBlock.class::isInstance);

        var builder = KubeJSCreateFabric.REGISTRATE.get()
                .tileEntity("cogwheel", BracketedKineticTileEntity::new)
                .instance(() -> CustomCogInstance::new, false)
                .renderer(() -> BracketedKineticTileRenderer::new);

        customCogs.forEach(it -> builder.validBlock(() -> it));
        customCogwheel = builder.register();
    }

}
