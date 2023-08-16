package dev.latvian.mods.kubejs.create.client;

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientHelper {
	public static void sandpaperClientStuff(SandPaperItem item) {
		BuiltinItemRendererRegistry.INSTANCE.register(item, new SandPaperItemRenderer());
		CustomRenderedItems.register(item);
	}
}
