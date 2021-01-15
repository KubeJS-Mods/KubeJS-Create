package dev.latvian.kubejs.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.kubejs.fluid.FluidStackJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import dev.latvian.kubejs.util.MapJS;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ProcessingRecipeJS extends RecipeJS
{
	public final List<FluidIngredient> inputFluids = new ArrayList<>();
	public final List<FluidStack> outputFluids = new ArrayList<>();

	@Override
	public void create(ListJS args)
	{
		for (Object o : ListJS.orSelf(args.get(0)))
		{
			if (o instanceof FluidStackJS)
			{
				CompoundNBT nbt = new CompoundNBT();
				((FluidStackJS) o).getFluidStack().write(nbt);
				outputFluids.add(FluidStack.loadFluidStackFromNBT(nbt));
			}
			else
			{
				outputItems.add(parseResultItem(o));
			}
		}

		for (Object o : ListJS.orSelf(args.get(1)))
		{
			if (o instanceof FluidStackJS)
			{
				CompoundNBT nbt = new CompoundNBT();
				((FluidStackJS) o).getFluidStack().write(nbt);
				inputFluids.add(FluidIngredient.fromFluidStack(FluidStack.loadFluidStackFromNBT(nbt)));
			}
			else if (o instanceof MapJS && (((MapJS) o).containsKey("fluid") || ((MapJS) o).containsKey("fluidTag")))
			{
				inputFluids.add(FluidIngredient.deserialize(((MapJS) o).toJson()));
			}
			else
			{
				inputItems.add(parseIngredientItem(o));
			}
		}

		json.addProperty("processingTime", 100);
	}

	@Override
	public void deserialize()
	{
		for (JsonElement e : JSONUtils.getJsonArray(json, "ingredients"))
		{
			if (FluidIngredient.isFluidIngredient(e))
			{
				inputFluids.add(FluidIngredient.deserialize(e));
			}
			else
			{
				inputItems.add(parseIngredientItem(e));
			}
		}

		for (JsonElement e : JSONUtils.getJsonArray(json, "results"))
		{
			JsonObject jsonObject = e.getAsJsonObject();

			if (JSONUtils.hasField(jsonObject, "fluid"))
			{
				outputFluids.add(FluidHelper.deserializeFluidStack(jsonObject));
			}
			else
			{
				outputItems.add(parseResultItem(e));
			}
		}
	}

	public ProcessingRecipeJS processingTime(int t)
	{
		json.addProperty("processingTime", t);
		save();
		return this;
	}

	public ProcessingRecipeJS heatRequirement(String req)
	{
		json.addProperty("heatRequirement", req);
		save();
		return this;
	}

	public ProcessingRecipeJS heated()
	{
		return heatRequirement("heated");
	}

	public ProcessingRecipeJS superheated()
	{
		return heatRequirement("superheated");
	}

	@Override
	public void serialize()
	{
		JsonArray jsonIngredients = new JsonArray();
		JsonArray jsonOutputs = new JsonArray();

		for (IngredientJS in : inputItems)
		{
			jsonIngredients.add(in.toJson());
		}

		for (FluidIngredient fs : inputFluids)
		{
			jsonIngredients.add(fs.serialize());
		}

		for (ItemStackJS is : outputItems)
		{
			jsonOutputs.add(is.toResultJson());
		}

		for (FluidStack fs : outputFluids)
		{
			jsonOutputs.add(FluidHelper.serializeFluidStack(fs));
		}

		json.add("ingredients", jsonIngredients);
		json.add("results", jsonOutputs);
	}
}
