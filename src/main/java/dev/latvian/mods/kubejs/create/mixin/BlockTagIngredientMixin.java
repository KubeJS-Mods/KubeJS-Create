package dev.latvian.mods.kubejs.create.mixin;

import com.simibubi.create.foundation.recipe.BlockTagIngredient;
import dev.latvian.mods.kubejs.item.ingredient.TagContext;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockTagIngredient.class)
public class BlockTagIngredientMixin {
	@Shadow
	@Final
	protected TagKey<Block> tag;

	@Shadow
	@Mutable
	protected ItemStack[] itemStacks;

	@Inject(method = "test(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/recipe/BlockTagIngredient;dissolve()V"), cancellable = true)
	public void dissolve(ItemStack item, CallbackInfoReturnable<Boolean> info) {
		if (RecipesEventJS.instance != null && itemStacks == null) {
			var context = TagContext.INSTANCE.getValue();
			for (var block : context.getTag(tag)) {
				if (item.is(block.value().asItem())) {
					info.setReturnValue(true);
					return;
				}
			}

			info.setReturnValue(false);
		}
	}
}
