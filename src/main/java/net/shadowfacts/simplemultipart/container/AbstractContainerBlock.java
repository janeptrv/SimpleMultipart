package net.shadowfacts.simplemultipart.container;

import net.minecraft.block.*;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.ExtendedBlockView;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.block.FabricBlockSettings;

import net.shadowfacts.simplemultipart.client.util.RenderStateProvider;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.util.MultipartHitResult;
import net.shadowfacts.simplemultipart.multipart.MultipartView;
import net.shadowfacts.simplemultipart.util.ShapeUtils;

import java.util.Set;

/**
 * @author shadowfacts
 */
public abstract class AbstractContainerBlock extends Block implements BlockEntityProvider, RenderStateProvider {

	public AbstractContainerBlock() {
		super(FabricBlockSettings.of(Material.STONE).build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		MultipartContainer container = (MultipartContainer)world.getBlockEntity(pos);
		if (container == null) {
			return false;
		}

		MultipartHitResult hit = MultipartHelper.rayTrace(container, world, pos, player);
		if (hit == null) {
			return false;
		}

		return hit.view.getState().activate(hit.view, hit.getSide(), player, hand);
	}

	@Override
	public BlockState getStateForRendering(BlockState state, BlockPos pos, ExtendedBlockView world) {
		MultipartContainer container = (MultipartContainer)world.getBlockEntity(pos);
		if (container == null) {
			return state;
		}

		Set<MultipartView> parts = container.getParts();
		return new ContainerBlockState(state, parts);
	}

	private VoxelShape getCombinedShape(BlockView world, BlockPos pos) {
		MultipartContainer container = (MultipartContainer)world.getBlockEntity(pos);
		if (container == null) {
			return VoxelShapes.empty();
		}

		VoxelShape shape = null;
		for (MultipartView view : container.getParts()) {
			VoxelShape partShape = view.getState().getBoundingShape(view);
			shape = shape == null ? partShape : VoxelShapes.union(shape, partShape);
		}
		return shape == null ? VoxelShapes.empty() : shape;
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, VerticalEntityPosition verticalEntityPosition) {
		return getCombinedShape(world, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, VerticalEntityPosition verticalEntityPosition) {
		return getCombinedShape(world, pos);
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView world, BlockPos pos) {
		return getCombinedShape(world, pos);
	}

//	@Override
//	@Deprecated
//	public boolean hasSolidTopSurface(BlockState state, BlockView world, BlockPos pos) {
//		MultipartContainer container = (MultipartContainer)world.getBlockEntity(pos);
//		if (container == null) {
//			return false;
//		}
//
//		return container.getParts().stream()
//				.anyMatch(view -> {
//					VoxelShape shape = view.getState().getBoundingShape(view);
//					return ShapeUtils.hasSolidSide(shape, Direction.UP);
//				});
//	}

	@Override
	public abstract AbstractContainerBlockEntity createBlockEntity(BlockView world);

	@Override
	@Deprecated
	public void onBlockRemoved(BlockState blockState_1, World world, BlockPos pos, BlockState blockState_2, boolean boolean_1) {
		super.onBlockRemoved(blockState_1, world, pos, blockState_2, boolean_1);
		if (blockState_1 != blockState_2) {
			world.removeBlockEntity(pos);
		}
	}
}
