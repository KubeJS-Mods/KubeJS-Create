package dev.latvian.mods.kubejs.create.custom;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.ItemDescription;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static com.simibubi.create.AllItems.SAND_PAPER;

public class SandpaperItemBuilder extends ItemBuilder {
	public SandpaperItemBuilder(ResourceLocation i) {
		super(i);
		tag(Create.asResource("sandpaper"));
	}

	@Override
	public Item createObject() {
		var item = new SandPaperItem(createItemProperties());
		ItemDescription.referKey(item, SAND_PAPER);
		return item;
	}
}
