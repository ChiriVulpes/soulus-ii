package chirichirichiri.soulus.ii.registry.registration;

import chirichirichiri.soulus.ii.utility.translation.Dictionary;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IRegistrationHasItem<T extends IForgeRegistryEntry<T>, I extends Item> extends IRegistration<T> {

	////////////////////////////////////
	// Item
	//

	public I getItem ();

	public default ItemStack getItemStack () {
		if (this instanceof Item)
			return new ItemStack((Item) this);

		if (this instanceof Block)
			return new ItemStack(((Block) this));

		throw new IllegalArgumentException("Must be called on a valid Block or Item");
	}

	public default ItemStack getItemStack (final Integer count) {
		ItemStack result = getItemStack();
		result.setCount(count);
		return result;
	}


	////////////////////////////////////
	// Description
	//

	public default boolean hasDescription () {
		return Dictionary.DESCRIPTION
			.translation(getDescriptionRegistryName())
			.exists();
	}

	public default String getDescription () {
		return Dictionary.DESCRIPTION
			.translation(getDescriptionRegistryName())
			.localise();
	}

	public default String getDescriptionRegistryName () {
		return getRegistryName().toString();
	}

	public default Ingredient getDescriptionIngredient () {
		return null;
	}
}
