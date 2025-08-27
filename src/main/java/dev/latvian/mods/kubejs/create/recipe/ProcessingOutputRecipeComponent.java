package dev.latvian.mods.kubejs.create.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.match.ItemMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.item.ItemStack;

public class ProcessingOutputRecipeComponent extends SimpleRecipeComponent<ProcessingOutput> {
	public static final TypeInfo TYPE_INFO = TypeInfo.of(ProcessingOutput.class);
	public static final RecipeComponentType<ProcessingOutput> TYPE = RecipeComponentType.unit(Create.asResource("processing_output"), ProcessingOutputRecipeComponent::new);

	public ProcessingOutputRecipeComponent(RecipeComponentType<?> type) {
		super(type, ProcessingOutput.CODEC, TYPE_INFO);
	}

	@Override
	public boolean hasPriority(Context cx, KubeRecipe recipe, Object from) {
		return from instanceof ProcessingOutput || ItemWrapper.isItemStackLike(from);
	}

	@Override
	public boolean matches(Context cx, KubeRecipe recipe, ProcessingOutput value, ReplacementMatchInfo match) {
		return match.match() instanceof ItemMatch m && m.matches(cx, value.getStack(), match.exact());
	}

	@Override
	public ProcessingOutput wrap(Context cx, KubeRecipe recipe, Object from) {
		if (from instanceof ProcessingOutput p) {
			return p;
		}

		var stack = (ItemStack) cx.jsToJava(from, ItemWrapper.TYPE_INFO);
		return stack == null || stack.isEmpty() ? ProcessingOutput.EMPTY : new ProcessingOutput(stack, 1F);
	}

	@Override
	public boolean isEmpty(ProcessingOutput value) {
		return value == ProcessingOutput.EMPTY || value.getStack().isEmpty();
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, ProcessingOutput value) {
		if (!isEmpty(value)) {
			builder.append(value.getStack().kjs$getIdLocation());
		}
	}
}
