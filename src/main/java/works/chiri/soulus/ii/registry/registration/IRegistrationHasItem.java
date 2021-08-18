package works.chiri.soulus.ii.registry.registration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;
import works.chiri.soulus.ii.utility.translation.Dictionary;


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
	// About Info
	//

	public default boolean hasAboutInfo () {
		return Dictionary.ABOUT_INFO.translation(getAboutInfoRegistryName()).exists();
	}

	public default String getAboutInfo () {
		return Dictionary.ABOUT_INFO.translation(getAboutInfoRegistryName()).localise();
	}

	public default String getAboutInfoRegistryName () {
		return getRegistryName().toString();
	}

	public default Ingredient getAboutInfoIngredient () {
		return null;
	}

}
