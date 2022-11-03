package dev.latvian.mods.kubejs.create.forge;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;
import dev.latvian.mods.kubejs.create.KubeJSCreate;
import dev.latvian.mods.kubejs.create.platform.CogWheelHelper;
import dev.latvian.mods.kubejs.create.platform.forge.CogWheelHelperImpl;
import net.minecraftforge.fml.common.Mod;

@Mod("kubejs_create")
public class KubeJSCreateForge {

	public static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy("kubejs");

	public KubeJSCreateForge() {
		KubeJSCreate.init();
		CogWheelHelperImpl.init();
	}

}