package works.chiri.soulus.ii.registry.registration.item;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;
import works.chiri.soulus.ii.client.SoulusIIItemGroup;
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

	public ItemFactory (final Function<Item.Properties, I> function) {
		super();
		supplier = () -> function.apply(createProperties());
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
		properties.tab(SoulusIIItemGroup.INSTANCE);

		if (inheritFrom != null) {
			final Food food = inheritFrom.getFoodProperties(); // fragile, can be overrided
			if (food != null)
				properties.food(food);

			final int maxStackSize = inheritFrom.getMaxStackSize();
			if (maxStackSize != 64) // grrr hardcoded
				properties.stacksTo(maxStackSize);

			final int maxDamage = inheritFrom.getMaxDamage();
			if (maxDamage != 0)
				properties.durability(maxDamage);

			final Item containerItem = inheritFrom.getCraftingRemainingItem();
			if (containerItem != null)
				properties.craftRemainder(containerItem);

			final ItemGroup group = inheritFrom.getItemCategory();
			if (group != null)
				properties.tab(group);

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
