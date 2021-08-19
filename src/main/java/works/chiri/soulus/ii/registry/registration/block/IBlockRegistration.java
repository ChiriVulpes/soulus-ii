package works.chiri.soulus.ii.registry.registration.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import works.chiri.soulus.ii.registry.registration.IRegistrationHasItem;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.registry.registration.Registration.BlockItem;


public interface IBlockRegistration<B extends Block, I extends Item> extends IRegistrationHasItem<Block, I> {

	public B getBlock ();


	////////////////////////////////////
	// Item
	//

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	public default I getItem () {
		final Item item = Item.BY_BLOCK.get(this);
		if (item != null)
			return (I) item;

		final net.minecraft.item.Item.Properties properties = new Item.Properties();
		itemProperties(properties);
		return (I) Registration.Item.Factory(BlockItem.class).properties(this::itemProperties).create();
	}

	public default void itemProperties (final net.minecraft.item.Item.Properties properties) {}

	////////////////////////////////////
	// Misc
	//

	public default Class<? extends TileEntity> getTileEntityClass () {
		return null;
	}

}
