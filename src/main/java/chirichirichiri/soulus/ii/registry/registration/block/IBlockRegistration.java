package chirichirichiri.soulus.ii.registry.registration.block;

import chirichirichiri.soulus.ii.registry.registration.IRegistrationHasItem;
import chirichirichiri.soulus.ii.registry.registration.Registration;
import chirichirichiri.soulus.ii.registry.registration.Registration.BlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public interface IBlockRegistration<B extends Block, I extends Item> extends IRegistrationHasItem<Block, I> {

	public B getBlock ();


	////////////////////////////////////
	// Item
	//

	@SuppressWarnings("unchecked")
	@Override
	public default I getItem () {
		final net.minecraft.item.Item.Properties properties = new Item.Properties();
		itemProperties(properties);
		return (I) Registration.Item.Factory(BlockItem.class)
			.properties(this::itemProperties)
			.create();
	}

	public default void itemProperties (final net.minecraft.item.Item.Properties properties) {
	}

	////////////////////////////////////
	// Misc
	//

	public default Class<? extends TileEntity> getTileEntityClass () {
		return null;
	}

}
