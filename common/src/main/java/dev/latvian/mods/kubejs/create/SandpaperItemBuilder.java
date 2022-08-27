package dev.latvian.mods.kubejs.create;

import com.simibubi.create.content.curiosities.tools.SandPaperItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SandpaperItemBuilder extends ItemBuilder {

	public SandpaperItemBuilder(ResourceLocation i) {
		super(i);
	}

	@Override
	public Item createObject() {
		return new SandPaperItem(createItemProperties());
	}

}
