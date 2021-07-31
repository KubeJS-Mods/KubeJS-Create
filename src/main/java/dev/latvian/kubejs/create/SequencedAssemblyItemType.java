package dev.latvian.kubejs.create;

import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import dev.latvian.kubejs.item.ItemBuilder;
import dev.latvian.kubejs.item.custom.ItemType;
import net.minecraft.world.item.Item;

public class SequencedAssemblyItemType extends ItemType {
	public SequencedAssemblyItemType(String n) {
		super(n);
	}

	@Override
	public Item createItem(ItemBuilder builder) {
		return new SequencedAssemblyItem(builder.createItemProperties());
	}
}
