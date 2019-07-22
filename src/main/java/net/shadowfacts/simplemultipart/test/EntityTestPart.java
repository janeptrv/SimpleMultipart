package net.shadowfacts.simplemultipart.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.container.AbstractContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntityProvider;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

/**
 * @author shadowfacts
 */
public class EntityTestPart extends Multipart implements MultipartEntityProvider {

	@Override
	@Deprecated
	public VoxelShape getBoundingShape(MultipartState state, MultipartView view) {
		return VoxelShapes.cuboid(6/16f, 6/16f, 6/16f, 10/16f, 10/16f, 10/16f);
	}

	@Override
	@Deprecated
	public boolean activate(MultipartView view, Direction side, PlayerEntity player, Hand hand) {
		BlockPos pos = ((Entity)view.getEntity()).getPos();
		player.addChatMessage(new LiteralText("Clicked: " + pos), false);
		return true;
	}

	@Override
	public MultipartEntity createMultipartEntity(MultipartState state, MultipartContainer container) {
		return new Entity();
	}

	public static class Entity extends MultipartEntity {
		public BlockPos getPos() {
			return ((AbstractContainerBlockEntity)view.getContainer()).getPos();
		}
	}

}
