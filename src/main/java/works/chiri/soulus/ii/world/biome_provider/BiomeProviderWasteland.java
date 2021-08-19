package works.chiri.soulus.ii.world.biome_provider;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.world.biome_provider.layer.LayerUtilWasteland;
import works.chiri.soulus.ii.world.biome_provider.layer.LayerWasteland;


public class BiomeProviderWasteland extends BiomeProvider {

	// wtf does any of this mean????? codec?? stable???? lifecycle??????
	public static final Codec<BiomeProviderWasteland> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("seed").stable().forGetter(provider -> provider.seed),
		Codec.BOOL.optionalFieldOf("legacy_biome_init_layer", Boolean.valueOf(false), Lifecycle.stable())
			.forGetter(provider -> provider.legacyBiomeInitLayer),
		Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter(provider -> provider.largeBiomes),
		RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.biomes))
		.apply(instance, instance.stable(BiomeProviderWasteland::new)));

	public static void register () {
		Registry.register(Registry.BIOME_SOURCE, SoulusII.location("biome_source_wasteland"), CODEC);
	}

	private final LayerWasteland noiseBiomeLayer;
	private static final List<RegistryKey<Biome>> POSSIBLE_BIOMES = ImmutableList.of(
		// put the new biomes here
		Biomes.BADLANDS);
	private final long seed;
	private final boolean legacyBiomeInitLayer;
	private final boolean largeBiomes;
	private final Registry<Biome> biomes;

	public BiomeProviderWasteland (
		final long seed,
		final boolean legacyBiomeInitLayer,
		final boolean largeBiomes,
		final Registry<Biome> biomes
	) {
		super(POSSIBLE_BIOMES.stream()
			.map(id -> () -> biomes.getOrThrow(id)));
		this.seed = seed;
		this.legacyBiomeInitLayer = legacyBiomeInitLayer;
		this.largeBiomes = largeBiomes;
		this.biomes = biomes;
		this.noiseBiomeLayer = LayerUtilWasteland.getDefaultLayer(seed, legacyBiomeInitLayer, largeBiomes ? 6 : 4, 4);
	}

	protected Codec<? extends BiomeProvider> codec () {
		return CODEC;
	}

	@OnlyIn(Dist.CLIENT)
	public BiomeProvider withSeed (final long seed) {
		return new BiomeProviderWasteland(seed, this.legacyBiomeInitLayer, this.largeBiomes, this.biomes);
	}

	public Biome getNoiseBiome (final int chunkX, final int chunkY, final int chunkZ) {
		return this.noiseBiomeLayer.get(this.biomes, chunkX, chunkZ);
	}

}
