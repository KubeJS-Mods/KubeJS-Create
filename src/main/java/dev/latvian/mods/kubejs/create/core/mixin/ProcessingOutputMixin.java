package dev.latvian.mods.kubejs.create.core.mixin;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.create.wrapper.KubeCreateOutput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProcessingOutput.class)
public class ProcessingOutputMixin implements KubeCreateOutput {
}
