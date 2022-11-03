package dev.latvian.mods.kubejs.create.platform.forge;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileEntity;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.create.cogwheel.CustomCogwheelBlock;
import dev.latvian.mods.kubejs.create.forge.CustomCogInstance;
import dev.latvian.mods.kubejs.create.forge.KubeJSCreateForge;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;

public class CogWheelHelperImpl {

    private static BlockEntityEntry<BracketedKineticTileEntity> customCogwheel;

    public static void registerModel(Block block) {
        CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new).accept(block);
    }

    public static BlockEntityType<BracketedKineticTileEntity> getCogWheel() {
        return customCogwheel.get();
    }

    private static void register(RegistryEvent.Register<Block> event) {
        var customBlocks = RegistryObjectBuilderTypes.BLOCK.deferredRegister.getRegistrar().entrySet();
        var customCogs = customBlocks.stream().map(Map.Entry::getValue).filter(CustomCogwheelBlock.class::isInstance);

        var builder = KubeJSCreateForge.REGISTRATE.get()
                .tileEntity("cogwheel", BracketedKineticTileEntity::new)
                .instance(() -> CustomCogInstance::new, false)
                .renderer(() -> BracketedKineticTileRenderer::new);

        customCogs.forEach(it -> builder.validBlock(() -> it));
        customCogwheel = builder.register();
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, EventPriority.HIGH, CogWheelHelperImpl::register);
    }

}
