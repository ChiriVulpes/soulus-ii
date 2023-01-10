package works.chiri.soulus.ii.server.serialisers;

import java.io.DataOutputStream;

import net.minecraft.block.BlockState;
import works.chiri.soulus.ii.server.Serialiser;
import works.chiri.soulus.ii.server.TypeScript;


public class BlockStateSerialiser extends Serialiser {

	{
		TypeScript.registerName(BlockState.class, "BlockState");
	}

	@Override
	public void handle (final DataOutputStream out, final Object object) {

	}

}
