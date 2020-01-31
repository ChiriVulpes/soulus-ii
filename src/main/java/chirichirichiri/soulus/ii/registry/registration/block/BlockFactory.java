package chirichirichiri.soulus.ii.registry.registration.block;

import java.util.function.Consumer;
import chirichirichiri.soulus.ii.registry.registration.Registration;
import chirichirichiri.soulus.ii.registry.registration.RegistrationFactory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;

public class BlockFactory<B extends Block> extends RegistrationFactory<Block, B, BlockFactory<B>> {

	public BlockFactory () {
	}

	public BlockFactory (final Class<B> cls) {
		super(cls);
	}

	public BlockFactory (final Block block) {
		inherit(block);
	}

	private Block inheritFrom = null;

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
		this.materialColour = colour.getMapColor();
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
			properties = Block.Properties.from(inheritFrom);

		if (material == null && properties == null)
			throw new IllegalStateException("Cannot create block properties without a material");

		MaterialColor materialColour = this.materialColour;
		if (materialColour == null && material != null)
			materialColour = material.getColor();

		properties = properties != null ? properties : Block.Properties.create(material, materialColour);
		if (propertiesInitialiser != null)
			propertiesInitialiser.accept(properties);

		return properties;
	}


	////////////////////////////////////
	// Initialisation
	//

	@Override
	protected Class<? extends Block> getDefaultRegistrationClass () {
		return Registration.Block.class;
	}

	@Override
	protected Class<?>[] getConstructorParameterTypes () {
		return new Class<?>[] {
			Block.Properties.class,
		};
	}

	@Override
	protected Object[] getConstructorParameters () {
		return new Object[] {
			(createProperties()),
		};
	}
}
