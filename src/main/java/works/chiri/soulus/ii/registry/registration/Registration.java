package works.chiri.soulus.ii.registry.registration;

import works.chiri.soulus.ii.registry.registration.block.BlockFactory;
import works.chiri.soulus.ii.registry.registration.block.IBlockRegistration;
import works.chiri.soulus.ii.registry.registration.item.IItemRegistration;
import works.chiri.soulus.ii.registry.registration.item.ItemFactory;


public class Registration {

	////////////////////////////////////
	// Blocks
	//

	public static class Block extends net.minecraft.block.Block implements IBlockRegistration<Block, BlockItem> {

		public static BlockFactory<Block> Factory () {
			return new BlockFactory<>(Block.class);
		}

		public static <B extends net.minecraft.block.Block> BlockFactory<B> Factory (final Class<B> cls) {
			return new BlockFactory<>(cls);
		}

		public Block (final net.minecraft.block.Block.Properties properties) {
			super(properties);
		}

		@Override
		public Block getBlock () {
			return this;
		}

	}


	////////////////////////////////////
	// Items
	//

	public static class Item extends net.minecraft.item.Item implements IItemRegistration<Item> {

		public static ItemFactory<Item> Factory () {
			return new ItemFactory<>(Item.class);
		}

		public static <I extends net.minecraft.item.Item> ItemFactory<I> Factory (final Class<I> cls) {
			return new ItemFactory<>(cls);
		}

		public Item (final Item.Properties properties) {
			super(properties);
		}

		@Override
		public Item getItem () {
			return this;
		}

	}

	public static class BlockItem extends net.minecraft.item.BlockItem implements IItemRegistration<BlockItem> {

		public BlockItem (final IBlockRegistration<?, ?> block, final Item.Properties properties) {
			super(block.getBlock(), properties);
		}

		@Override
		public BlockItem getItem () {
			return this;
		}

	}

}
