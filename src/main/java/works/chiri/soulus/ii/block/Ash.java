package works.chiri.soulus.ii.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import works.chiri.soulus.ii.registry.Register;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.registry.registration.Registration.Block;


public class Ash {

	@Register.Block("block_ash")
	public static final Registration.Block ASH = Registration.Block.Factory()
		.mapColor(DyeColor.GRAY)
		.material(Material.SNOW)
		.properties(properties -> properties
			.harvestTool(ToolType.SHOVEL)
			.strength(0.2F)
			.sound(SoundType.SNOW))
		.create();

	@Register.Block("block_ash_layer")
	public static final Registration.Block ASH_LAYER = Registration.Block.Factory(AshBlock.class)
		.mapColor(DyeColor.GRAY)
		.material(Material.TOP_SNOW)
		.properties(properties -> properties
			.harvestTool(ToolType.SHOVEL)
			.strength(0.1F)
			.sound(SoundType.SNOW))
		.create();


	public static class AshBlock extends Registration.Block {

		public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
		protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[] { VoxelShapes.empty(),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

		public AshBlock (final Block.Properties properties) {
			super(properties);
			this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)));
		}

		public boolean isPathfindable (
			final BlockState blockState,
			final IBlockReader reader,
			final BlockPos pos,
			final PathType pathType
		) {
			return pathType == PathType.LAND && blockState.getValue(LAYERS) < 5;
		}

		public VoxelShape getShape (
			final BlockState blockState,
			final IBlockReader reader,
			final BlockPos pos,
			final ISelectionContext context
		) {
			return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
		}

		public VoxelShape getCollisionShape (
			final BlockState blockState,
			final IBlockReader reader,
			final BlockPos pos,
			final ISelectionContext context
		) {
			return SHAPE_BY_LAYER[blockState.getValue(LAYERS) - 1];
		}

		public VoxelShape getBlockSupportShape (
			final BlockState blockState,
			final IBlockReader reader,
			final BlockPos pos
		) {
			return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
		}

		public VoxelShape getVisualShape (
			final BlockState blockState,
			final IBlockReader reader,
			final BlockPos pos,
			final ISelectionContext context
		) {
			return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
		}

		public boolean useShapeForLightOcclusion (final BlockState blockState) {
			return true;
		}

		public boolean canSurvive (
			final BlockState blockState,
			final IWorldReader reader,
			final BlockPos pos
		) {
			final BlockState stateBelow = reader.getBlockState(pos.below());
			if (stateBelow.is(Blocks.ICE) || stateBelow.is(Blocks.PACKED_ICE) || stateBelow.is(Blocks.BARRIER))
				return false;

			if (stateBelow.is(Blocks.HONEY_BLOCK) || stateBelow.is(Blocks.SOUL_SAND))
				return true; // these blocks don't have full faces, but we explicitly survive on them

			return Block.isFaceFull(stateBelow.getCollisionShape(reader, pos.below()), Direction.UP)
				|| stateBelow.getBlock() == this && stateBelow.getValue(LAYERS) == 8;
		}

		@SuppressWarnings("deprecation")
		public BlockState updateShape (
			final BlockState ownState,
			final Direction updatedDirection,
			final BlockState updatedState,
			final IWorld world,
			final BlockPos ownPos,
			final BlockPos updatedPos
		) {
			if (!ownState.canSurvive(world, ownPos))
				return Blocks.AIR.defaultBlockState();

			return super.updateShape(ownState, updatedDirection, updatedState, world, ownPos, updatedPos);
		}

		public void randomTick (
			final BlockState blockState,
			final ServerWorld world,
			final BlockPos pos,
			final Random random
		) {
			if (world.getBrightness(LightType.BLOCK, pos) > 11) {
				dropResources(blockState, world, pos);
				world.removeBlock(pos, false);
			}

		}

		public boolean canBeReplaced (final BlockState blockState, final BlockItemUseContext context) {
			final int i = blockState.getValue(LAYERS);
			if (context.getItemInHand().getItem() != this.asItem() || i >= 8)
				return i == 1;

			return !context.replacingClickedOnBlock()
				|| context.getClickedFace() == Direction.UP;
		}

		@Nullable
		public BlockState getStateForPlacement (final BlockItemUseContext context) {
			final BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
			if (!blockState.is(this))
				return super.getStateForPlacement(context);

			final int i = blockState.getValue(LAYERS);
			return blockState.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
		}

		@Override
		protected void createBlockStateDefinition (final Builder<net.minecraft.block.Block, BlockState> definition) {
			definition.add(LAYERS);
		}

	}

}
