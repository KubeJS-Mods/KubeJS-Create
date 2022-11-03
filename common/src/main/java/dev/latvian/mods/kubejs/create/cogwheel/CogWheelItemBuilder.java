package dev.latvian.mods.kubejs.create.cogwheel;

import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import com.simibubi.create.content.contraptions.relays.elementary.CogwheelBlockItem;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CogWheelItemBuilder extends BlockItemBuilder {

    public CogWheelItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Item createObject() {
        var block = blockBuilder.get();
        if (block instanceof CogWheelBlock cog) {
            return new CogwheelBlockItem(cog, createItemProperties());
        } else {
            return null;
        }
    }
}
