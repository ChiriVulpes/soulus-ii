package works.chiri.soulus.ii.registry.registration.block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import works.chiri.soulus.ii.client.SoulusIIItemGroup;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.registry.registration.RegistrationFactory;
import works.chiri.soulus.ii.registry.registration.item.ItemFactory;


@SuppressWarnings("unchecked")
public class BlockFactory<B extends Block> extends RegistrationFactory<Block, B, BlockFactory<B>> {

	public BlockFactory () {}

	public BlockFactory (final Class<B> cls) {
		super(cls);
	}

	public BlockFactory (final Block block) {
		inherit(block);
	}

	private Block inheritFrom = null;

	@Nullable
	private ItemGroup group = SoulusIIItemGroup.INSTANCE;

	public BlockFactory<B> inherit (final Block block) {
		inheritFrom = block;
		return this;
	}

	////////////////////////////////////
	// Material & Map Colour
	//

	private Material material = null;
	private MaterialColor materialColour = null;

	public BlockFactory<B> material (final Material material) {
		this.material = material;
		return this;
	}

	public BlockFactory<B> mapColor (final MaterialColor colour) {
		this.materialColour = colour;
		return this;
	}

	public BlockFactory<B> mapColor (final DyeColor colour) {
		this.materialColour = colour.getMaterialColor();
		return this;
	}


	////////////////////////////////////
	// Properties
	//

	private Consumer<Block.Properties> propertiesInitialiser = null;

	public BlockFactory<B> properties (final Consumer<Block.Properties> initialiser) {
		propertiesInitialiser = initialiser;
		return this;
	}

	public Block.Properties createProperties () {
		Block.Properties properties = null;
		if (inheritFrom != null)
			properties = Block.Properties.copy(inheritFrom);

		if (material == null && properties == null)
			throw new IllegalStateException("Cannot create block properties without a material");

		MaterialColor materialColour = this.materialColour;
		if (materialColour == null && material != null)
			materialColour = material.getColor();

		properties = properties != null ? properties : Block.Properties.of(material, materialColour);
		if (propertiesInitialiser != null)
			propertiesInitialiser.accept(properties);

		return properties;
	}


	////////////////////////////////////
	// Item
	//

	private IItemFactoryFunction<B> itemFactoryFunction = (factory, block) -> {};

	/**
	 * @param function A function that will build the item, or `null` if the block should have no item.
	 */
	public BlockFactory<B> item (@Nullable final IItemFactoryFunction<B> function) {
		itemFactoryFunction = function;
		return this;
	}

	public static interface IItemFactoryFunction<B extends Block> {

		public void apply (final ItemFactory<Registration.BlockItem> factory, final B block);

	}


	////////////////////////////////////
	// Initialisation
	//

	public static final Map<Block, Registration.BlockItem> ITEMS_BY_BLOCK = new HashMap<>();

	@Override
	protected void initialise (final Block registration) {
		if (itemFactoryFunction == null)
			return;

		final ItemFactory<Registration.BlockItem> itemFactory = Registration.Item
			.Factory(properties -> new Registration.BlockItem(registration, properties));
		itemFactoryFunction.apply(itemFactory, (B) registration);
		ITEMS_BY_BLOCK.put(registration, itemFactory.create());
	}

	@Override
	protected Class<? extends Block> getDefaultRegistrationClass () {
		return Registration.Block.class;
	}

	@Override
	protected Class<?>[] getConstructorParameterTypes () {
		return new Class<?>[] { Block.Properties.class, };
	}

	@Override
	protected Object[] getConstructorParameters () {
		return new Object[] { (createProperties()), };
	}

}
