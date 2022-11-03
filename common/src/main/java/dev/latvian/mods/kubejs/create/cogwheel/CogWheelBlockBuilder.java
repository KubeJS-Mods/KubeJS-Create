package dev.latvian.mods.kubejs.create.cogwheel;

import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.create.platform.CogWheelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CogWheelBlockBuilder extends BlockBuilder {

    private boolean isLarge = false;

    public CogWheelBlockBuilder(ResourceLocation i) {
        super(i);
        itemBuilder = new CogWheelItemBuilder(i);
        itemBuilder.blockBuilder = this;
    }

    public void small() {
        isLarge = false;
    }

    public void large() {
        isLarge = true;
    }

    @Override
    public Block createObject() {
        return new CustomCogwheelBlock(isLarge, createProperties());
    }

    @Override
    public void clientRegistry(Minecraft minecraft) {
        super.clientRegistry(minecraft);
        CogWheelHelper.registerModel(asRegistrySupplier().get());
    }
}
