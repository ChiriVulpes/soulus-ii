package works.chiri.soulus.ii.world.biome_provider.layer;

import java.util.function.LongFunction;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.AddBambooForestLayer;
import net.minecraft.world.gen.layer.AddIslandLayer;
import net.minecraft.world.gen.layer.AddMushroomIslandLayer;
import net.minecraft.world.gen.layer.AddSnowLayer;
import net.minecraft.world.gen.layer.BiomeLayer;
import net.minecraft.world.gen.layer.DeepOceanLayer;
import net.minecraft.world.gen.layer.EdgeBiomeLayer;
import net.minecraft.world.gen.layer.EdgeLayer;
import net.minecraft.world.gen.layer.HillsLayer;
import net.minecraft.world.gen.layer.IslandLayer;
import net.minecraft.world.gen.layer.MixOceansLayer;
import net.minecraft.world.gen.layer.MixRiverLayer;
import net.minecraft.world.gen.layer.OceanLayer;
import net.minecraft.world.gen.layer.RareBiomeLayer;
import net.minecraft.world.gen.layer.RemoveTooMuchOceanLayer;
import net.minecraft.world.gen.layer.RiverLayer;
import net.minecraft.world.gen.layer.ShoreLayer;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.StartRiverLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;


public class LayerUtilWasteland {

	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> zoom (
		final long amount,
		final IAreaTransformer1 transformer,
		IAreaFactory<T> area,
		final int times,
		final LongFunction<C> noiseGenerator
	) {
		for (int i = 0; i < times; ++i)
			area = transformer.run(noiseGenerator.apply(amount + (long) i), area);

		return area;
	}

	private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getDefaultLayer (
		final boolean legacyDesert,
		final int islandAmount,
		final int preRiverZoomAmount,
		final LongFunction<C> noiseGenerator
	) {
		IAreaFactory<T> area = IslandLayer.INSTANCE.run(noiseGenerator.apply(1L));
		area = ZoomLayer.FUZZY.run(noiseGenerator.apply(2000L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(1L), area);
		area = ZoomLayer.NORMAL.run(noiseGenerator.apply(2001L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(2L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(50L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(70L), area);
		area = RemoveTooMuchOceanLayer.INSTANCE.run(noiseGenerator.apply(2L), area);

		IAreaFactory<T> oceanArea = OceanLayer.INSTANCE.run(noiseGenerator.apply(2L));
		oceanArea = zoom(2001L, ZoomLayer.NORMAL, oceanArea, 6, noiseGenerator);

		area = AddSnowLayer.INSTANCE.run(noiseGenerator.apply(2L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(3L), area);
		area = EdgeLayer.CoolWarm.INSTANCE.run(noiseGenerator.apply(2L), area);
		area = EdgeLayer.HeatIce.INSTANCE.run(noiseGenerator.apply(2L), area);
		area = EdgeLayer.Special.INSTANCE.run(noiseGenerator.apply(3L), area);
		area = ZoomLayer.NORMAL.run(noiseGenerator.apply(2002L), area);
		area = ZoomLayer.NORMAL.run(noiseGenerator.apply(2003L), area);
		area = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(4L), area);
		area = AddMushroomIslandLayer.INSTANCE.run(noiseGenerator.apply(5L), area);
		area = DeepOceanLayer.INSTANCE.run(noiseGenerator.apply(4L), area);
		area = zoom(1000L, ZoomLayer.NORMAL, area, 0, noiseGenerator);

		IAreaFactory<T> riverArea = zoom(1000L, ZoomLayer.NORMAL, area, 0, noiseGenerator);
		riverArea = StartRiverLayer.INSTANCE.run(noiseGenerator.apply(100L), riverArea);

		IAreaFactory<T> rareArea = (new BiomeLayer(legacyDesert)).run(noiseGenerator.apply(200L), area);
		rareArea = AddBambooForestLayer.INSTANCE.run(noiseGenerator.apply(1001L), rareArea);
		rareArea = zoom(1000L, ZoomLayer.NORMAL, rareArea, 2, noiseGenerator);
		rareArea = EdgeBiomeLayer.INSTANCE.run(noiseGenerator.apply(1000L), rareArea);

		final IAreaFactory<T> hillsArea = zoom(1000L, ZoomLayer.NORMAL, riverArea, 2, noiseGenerator);
		rareArea = HillsLayer.INSTANCE.run(noiseGenerator.apply(1000L), rareArea, hillsArea);

		riverArea = zoom(1000L, ZoomLayer.NORMAL, riverArea, 2, noiseGenerator);
		riverArea = zoom(1000L, ZoomLayer.NORMAL, riverArea, preRiverZoomAmount, noiseGenerator);
		riverArea = RiverLayer.INSTANCE.run(noiseGenerator.apply(1L), riverArea);
		riverArea = SmoothLayer.INSTANCE.run(noiseGenerator.apply(1000L), riverArea);

		rareArea = RareBiomeLayer.INSTANCE.run(noiseGenerator.apply(1001L), rareArea);

		for (int i = 0; i < islandAmount; ++i) {
			rareArea = ZoomLayer.NORMAL.run(noiseGenerator.apply((long) (1000 + i)), rareArea);
			if (i == 0) {
				rareArea = AddIslandLayer.INSTANCE.run(noiseGenerator.apply(3L), rareArea);
			}

			if (i == 1 || islandAmount == 1) {
				rareArea = ShoreLayer.INSTANCE.run(noiseGenerator.apply(1000L), rareArea);
			}

		}

		rareArea = SmoothLayer.INSTANCE.run(noiseGenerator.apply(1000L), rareArea);
		rareArea = MixRiverLayer.INSTANCE.run(noiseGenerator.apply(100L), rareArea, riverArea);
		return MixOceansLayer.INSTANCE.run(noiseGenerator.apply(100L), rareArea, oceanArea);
	}

	public static LayerWasteland getDefaultLayer (
		final long seed1,
		final boolean legacyDesert,
		final int islandAmount,
		final int preRiverZoomAmount
	) {
		final IAreaFactory<LazyArea> area = getDefaultLayer(legacyDesert, islandAmount, preRiverZoomAmount,
			seed2 -> new LazyAreaLayerContext(25, seed1, seed2));
		return new LayerWasteland(area);
	}

}
