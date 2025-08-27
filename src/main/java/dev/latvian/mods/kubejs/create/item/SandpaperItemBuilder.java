package dev.latvian.mods.kubejs.create.item;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.ItemDescription;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SandpaperItemBuilder extends ItemBuilder {
	public SandpaperItemBuilder(ResourceLocation i) {
		super(i);
		tag(new ResourceLocation[]{AllTags.AllItemTags.SANDPAPER.tag.location()});
	}

	@Override
	public Item createObject() {
		var item = new SandPaperItem(createItemProperties());
		ItemDescription.referKey(item, AllItems.SAND_PAPER);
		return item;
	}
}
