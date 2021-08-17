package chirichirichiri.soulus.ii.world;

import chirichirichiri.soulus.ii.SoulusII;
import chirichirichiri.soulus.ii.world.generator.ChunkGeneratorWasteland;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;

public class Wasteland extends WorldType {

	public Wasteland () {
		super(SoulusII.ID + ":wasteland");
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator (final World world) {
		final DimensionType type = world.getDimension().getType();
		if (type == DimensionType.OVERWORLD)
			return new ChunkGeneratorWasteland(world);

		return super.createChunkGenerator(world);
	}
}
