package works.chiri.soulus.ii.server.actions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.server.ActionManager.Action;


public class Blocks {

	@Action({ "x", "y", "z", "block" })
	public static boolean set (final int x, final int y, final int z, final String block) {
		final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();
		final BlockStateParser parser = new BlockStateParser(new StringReader(block), true);
		try {
			parser.parse(true);
		}
		catch (final CommandSyntaxException e) {
			SoulusII.LOGGER.error("Unable to set block", e);
			return false;
		}

		return world.setBlock(new BlockPos(x, y, z), parser.getState(), BlockFlags.DEFAULT);
	}

	@Action({ "x", "y", "z" })
	public static BlockState get (final int x, final int y, final int z) {
		final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();

		return world.getBlockState(new BlockPos(x, y, z));
	}

}
