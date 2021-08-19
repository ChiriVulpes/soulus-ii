package works.chiri.soulus.ii.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import works.chiri.soulus.ii.registry.Register;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.world.biome_provider.BiomeProviderWasteland;
import works.chiri.soulus.ii.world.dimension.DimensionSettingsWasteland;


public class Wasteland {

	@Register.WorldType("wasteland")
	public static final Registration.WorldType INSTANCE = new Registration.WorldType(Wasteland::createChunkGenerator) {

		public void onRegistration () {
			DimensionSettingsWasteland.register();
			BiomeProviderWasteland.register();
		}

	};

	public static ChunkGenerator createChunkGenerator (
		final Registry<Biome> biomeRegistry, final Registry<DimensionSettings> dimensionSettingsRegistry,
		final long seed, final String generatorSettings
	) {
		final BiomeProviderWasteland provider = new BiomeProviderWasteland(seed, false, false, biomeRegistry);
		return new NoiseChunkGenerator(provider, seed,
			() -> dimensionSettingsRegistry.getOrThrow(DimensionSettingsWasteland.ID));
	}

}
