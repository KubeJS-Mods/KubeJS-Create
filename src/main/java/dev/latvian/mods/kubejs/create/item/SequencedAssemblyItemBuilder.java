package dev.latvian.mods.kubejs.create.item;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SequencedAssemblyItemBuilder extends ItemBuilder {
	public SequencedAssemblyItemBuilder(ResourceLocation i) {
		super(i);
	}

	@Override
	public Item createObject() {
		return new SequencedAssemblyItem(createItemProperties());
	}
}
