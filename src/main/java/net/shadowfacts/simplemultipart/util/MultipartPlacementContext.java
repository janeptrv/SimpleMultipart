package net.shadowfacts.simplemultipart.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import net.shadowfacts.simplemultipart.container.MultipartContainer;

/**
 * Contains information about the context in which a multipart is being placed into the world.
 *
 * Analogous to {@link net.minecraft.item.ItemPlacementContext}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public class MultipartPlacementContext extends ItemUsageContext {

	private final MultipartContainer container;
	private final boolean isOffset;

	public MultipartPlacementContext(MultipartContainer container, boolean isOffset, PlayerEntity player, ItemStack stack, BlockHitResult hitResult) {
		super(player, stack, hitResult);
		this.container = container;
		this.isOffset = isOffset;
	}

	public MultipartPlacementContext(MultipartContainer container, boolean isOffset, ItemUsageContext context) {
		this(container, isOffset, context.getPlayer(), context.getItemStack(), new BlockHitResult(context.getPos(), context.getFacing(), context.getBlockPos(), context.method_17699()));
	}

	/**
	 * @return The container that this multipart will be inserted into.
	 */
	public MultipartContainer getContainer() {
		return container;
	}

	/**
	 * @return {@code false} if this container is the one clicked, {@code true} if this container is a newly created one offset from the block clicked.
	 * @since 0.1.2
	 */
	public boolean isOffset() {
		return isOffset;
	}
}
