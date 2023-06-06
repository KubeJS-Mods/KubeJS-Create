package dev.latvian.mods.kubejs.create;

public class ItemApplicationRecipeJS extends ProcessingRecipeJS {
	public ItemApplicationRecipeJS keepHeldItem(boolean keep) {
		json.addProperty("keepHeldItem", keep);
		save();
		return this;
	}

	public ItemApplicationRecipeJS keepHeldItem() {
		return keepHeldItem(true);
	}
}
