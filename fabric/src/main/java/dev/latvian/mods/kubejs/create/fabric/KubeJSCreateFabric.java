package dev.latvian.mods.kubejs.create.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.latvian.mods.kubejs.create.KubeJSCreate;
import dev.latvian.mods.kubejs.create.platform.fabric.CogWheelHelperImpl;
import net.fabricmc.api.ModInitializer;

public class KubeJSCreateFabric implements ModInitializer {

    public static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy("kubejs");

    @Override
    public void onInitialize() {
        KubeJSCreate.init();
        CogWheelHelperImpl.register();
    }

}
