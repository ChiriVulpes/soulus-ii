package works.chiri.soulus.ii.world.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import works.chiri.soulus.ii.SoulusII;


public class DimensionSettingsWasteland {

	public static final RegistryKey<DimensionSettings> ID = RegistryKey
		.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY,
			SoulusII.location("dimension_settings_wasteland"));

	public static void register () {
		final double d0 = 0.9999999814507745D;
		final NoiseSettings noise = new NoiseSettings(
			256, // height
			new ScalingSettings(d0, d0, 80.0D, 160.0D), // noiseSamplingSettings
			new SlideSettings(-10, 3, 0), // topSlideSettings
			new SlideSettings(-30, 0, 0), // bottomSlideSettings
			1, // noiseSizeHorizontal
			2, // noiseSizeVertical
			1.0D, // densityFactor
			-0.46875D, // densityOffset
			true, // useSimplexSurfaceNoise
			true, // randomDensityOffset
			false, // islandNoiseOverride
			false // isAmplified
		);
		final DimensionStructuresSettings structures = new DimensionStructuresSettings(false);
		final DimensionSettings settings = new DimensionSettings(
			structures,
			noise,
			Blocks.STONE.defaultBlockState(), // defaultBlock
			Blocks.WATER.defaultBlockState(), // defaultFluid
			-10, // bedrockRoofPosition
			0, // bedrockFloorPosition
			63, // seaLevel
			false // disableMobGeneration
		);
		WorldGenRegistries.register(WorldGenRegistries.NOISE_GENERATOR_SETTINGS, ID.location(), settings);
	}

}
