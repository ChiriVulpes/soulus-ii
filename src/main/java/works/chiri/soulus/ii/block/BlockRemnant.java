package works.chiri.soulus.ii.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;
import works.chiri.soulus.ii.registry.Register;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.tag.Tags;


public class BlockRemnant {

	@Register.Block("block_remnant")
	public static final Registration.Block REMNANT = Registration.Block.Factory()
		.mapColor(DyeColor.BROWN)
		.material(Material.DIRT)
		.properties(properties -> properties
			.harvestTool(ToolType.SHOVEL)
			.strength(0.5F)
			.sound(SoundType.GRAVEL))
		.create();

	@Register.Block("block_remnant_grass")
	public static final Registration.Block GRASS = Registration.Block.Factory(BlockRemnantGrass.class)
		.mapColor(DyeColor.YELLOW)
		.material(Material.GRASS)
		.properties(properties -> properties
			.harvestTool(ToolType.SHOVEL)
			.strength(0.6F)
			.sound(SoundType.GRASS))
		.create();

	public static class BlockRemnantGrass extends Registration.Block {

		public static final BooleanProperty ASHEN = BooleanProperty.create("ashen");

		public BlockRemnantGrass (final Properties properties) {
			super(properties);
			this.registerDefaultState(this.stateDefinition.any().setValue(ASHEN, Boolean.valueOf(false)));
		}

		@SuppressWarnings("deprecation")
		public BlockState updateShape (
			final BlockState state,
			final Direction updatedDirection,
			final BlockState updatedState,
			final IWorld world,
			final BlockPos ownPosition,
			final BlockPos updatedPosition
		) {
			if (updatedDirection != Direction.UP)
				return super.updateShape(state, updatedDirection, updatedState, world, ownPosition, updatedPosition);

			return state.setValue(ASHEN, Boolean.valueOf(isAsh(updatedState)));
		}

		public BlockState getStateForPlacement (final BlockItemUseContext context) {
			final BlockState stateAbove = context.getLevel().getBlockState(context.getClickedPos().above());
			return this.defaultBlockState().setValue(ASHEN, Boolean.valueOf(isAsh(stateAbove)));
		}

		protected void createBlockStateDefinition (final StateContainer.Builder<Block, BlockState> definition) {
			definition.add(ASHEN);
		}

		private boolean isAsh (final BlockState blockState) {
			return blockState.is(Tags.ASH);
		}

	}

}
