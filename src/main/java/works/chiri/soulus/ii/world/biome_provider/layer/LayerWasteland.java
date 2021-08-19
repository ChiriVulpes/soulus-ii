package works.chiri.soulus.ii.world.biome_provider.layer;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import works.chiri.soulus.ii.SoulusII;


public class LayerWasteland {

	private final LazyArea area;

	public LayerWasteland (final IAreaFactory<LazyArea> area) {
		this.area = area.make();
	}

	private final Set<RegistryKey<Biome>> loggedBiomes = new HashSet<>();

	public Biome get (final Registry<Biome> biomes, final int x, final int y) {
		final int i = this.area.get(x, y);
		final RegistryKey<Biome> id = BiomeRegistry.byId(i);
		if (id == null)
			throw new IllegalStateException("Unknown biome id emitted by layers: " + i);

		if (loggedBiomes.add(id))
			SoulusII.LOGGER.info(id.location().toString());

		Biome biome = biomes.get(Biomes.BASALT_DELTAS);
		if (biome != null)
			return biome;

		SoulusII.LOGGER.warn("Unknown biome id: ", (int) i);
		return biomes.get(BiomeRegistry.byId(0));
	}

}
