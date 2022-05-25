package dev.latvian.mods.kubejs.create;

import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
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
