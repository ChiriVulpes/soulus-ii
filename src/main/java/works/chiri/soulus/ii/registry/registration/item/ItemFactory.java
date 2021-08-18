package works.chiri.soulus.ii.registry.registration.item;

import java.util.function.Consumer;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.registry.registration.RegistrationFactory;


public class ItemFactory<I extends Item> extends RegistrationFactory<Item, I, ItemFactory<I>> {

	public ItemFactory () {}

	public ItemFactory (final Class<I> cls) {
		super(cls);
	}

	public ItemFactory (final Item item) {
		inherit(item);
	}

	private Item inheritFrom = null;

	public ItemFactory<I> inherit (final Item item) {
		inheritFrom = item;
		return this;
	}

	////////////////////////////////////
	// Properties
	//

	private Consumer<Item.Properties> propertiesInitialiser = null;

	public ItemFactory<I> properties (final Consumer<Item.Properties> initializer) {
		propertiesInitialiser = initializer;
		return this;
	}

	@SuppressWarnings("deprecation")
	public Item.Properties createProperties () {
		final Item.Properties properties = new Item.Properties();

		if (inheritFrom != null) {
			final Food food = inheritFrom.getFood(); // fragile, can be overrided
			if (food != null)
				properties.food(food);

			final int maxStackSize = inheritFrom.getMaxStackSize();
			if (maxStackSize != 64) // grrr hardcoded
				properties.maxStackSize(maxStackSize);

			final int maxDamage = inheritFrom.getMaxDamage();
			if (maxDamage != 0)
				properties.maxDamage(maxDamage);

			final Item containerItem = inheritFrom.getContainerItem();
			if (containerItem != null)
				properties.containerItem(containerItem);

			final ItemGroup group = inheritFrom.getGroup();
			if (group != null)
				properties.group(group);

			final Rarity rarity = inheritFrom.getRarity(ItemStack.EMPTY); // fragile, can be overrided
			if (rarity != null)
				properties.rarity(rarity);

			if (!inheritFrom.isRepairable(ItemStack.EMPTY)) // fragile, can be overrided
				properties.setNoRepair();

			for (final ToolType type : inheritFrom.getToolTypes(ItemStack.EMPTY)) // fragile, can be overrided
				properties.addToolType(type, inheritFrom.getHarvestLevel(ItemStack.EMPTY, type, null, null)); // fragile, can be overrided

			properties.setISTER( () -> inheritFrom::getItemStackTileEntityRenderer);
		}

		if (propertiesInitialiser != null)
			propertiesInitialiser.accept(properties);

		return properties;
	}


	////////////////////////////////////
	// Initialisation
	//

	@Override
	protected Class<? extends Item> getDefaultRegistrationClass () {
		return Registration.Item.class;
	}

	@Override
	protected Class<?>[] getConstructorParameterTypes () {
		return new Class<?>[] { Item.Properties.class, };
	}

	@Override
	protected Object[] getConstructorParameters () {
		return new Object[] { (createProperties()), };
	}

}
