package dev.latvian.mods.kubejs.create.forge;

import dev.latvian.mods.kubejs.create.KubeJSCreate;
import net.minecraftforge.fml.common.Mod;

@Mod("kubejs_create")
public class KubeJSCreateForge {
	public KubeJSCreateForge() {
		KubeJSCreate.init();
	}
}