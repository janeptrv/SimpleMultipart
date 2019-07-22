package net.shadowfacts.simplemultipart.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartPlacementContext;

/**
 * An {@link Item} implementation that handles placing multiparts.
 *
 * Analogous to {@link net.minecraft.item.BlockItem}.
 *
 * @author shadowfacts
 * @since 0.1.1
 */
public class MultipartItem extends Item {

	protected Multipart part;

	/**
	 * Creates a Multipart Item for the given part with.
	 *
	 * @param part The multipart.
	 */
	public MultipartItem(Multipart part) {
		this(part, new Settings());
	}

	/**
	 * Creates a Multipart Item for the given part with the given item settings.
	 *
	 * @param part The multipart.
	 * @param settings The settings for this item.
	 * @since 0.1.2
	 */
	public MultipartItem(Multipart part, Settings settings) {
		super(settings);
		this.part = part;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return tryPlace(context);
	}

	/**
	 * Attempts to place this item's multipart in the given context.
	 *
	 * If the player clicked an existing multipart container, it will attempt to insert into that one, falling back on
	 * creating a new container.
	 * Which container the placement is being attempted in can be determined from {@link MultipartPlacementContext#isOffset()}.
	 *
	 * @param context The item usage context.
	 * @return The result of the placement.
	 */
	protected ActionResult tryPlace(ItemUsageContext context) {
		// If a multipart inside an existing container was clicked, try inserting into that
		MultipartContainer hitContainer = getContainer(context);
		if (hitContainer != null && tryPlace(new MultipartPlacementContext(hitContainer, false, context))) {
			return ActionResult.SUCCESS;
		}

		// Otherwise, get or create a new container and try inserting into that
		BlockHitResult offsetHitResult = new BlockHitResult(context.getHitPos(), context.getPlayerFacing(), context.getBlockPos().offset(context.getPlayerFacing()), context.method_17699());
		ItemUsageContext offsetContext = new ItemUsageContext(context.getPlayer(), context.getHand(), offsetHitResult);
		MultipartContainer offsetContainer = getOrCreateContainer(offsetContext);
		if (offsetContainer != null) {
			if (tryPlace(new MultipartPlacementContext(offsetContainer, true, offsetContext))) {
				return ActionResult.SUCCESS;
			} else {
				// if the a new container was created, and no part was inserted, remove the empty container
				if (!offsetContainer.hasParts()) {
					context.getWorld().setBlockState(offsetContext.getBlockPos(), Blocks.AIR.getDefaultState());
				}
			}
		}

		return ActionResult.FAIL;
	}

	protected MultipartContainer getContainer(ItemUsageContext context) {
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if (state.getBlock() == SimpleMultipart.containerBlock) {
			return (MultipartContainer)context.getWorld().getBlockEntity(context.getBlockPos());
		} else {
			return null;
		}
	}

	protected MultipartContainer getOrCreateContainer(ItemUsageContext context) {
		MultipartContainer container = getContainer(context);
		if (container == null) {
			BlockState existing = context.getWorld().getBlockState(context.getBlockPos());
			if (existing.isAir()) { // TODO: should check is replaceable (might not be mapped?)
				context.getWorld().setBlockState(context.getBlockPos(), SimpleMultipart.containerBlock.getDefaultState());
				container = (MultipartContainer)context.getWorld().getBlockEntity(context.getBlockPos());
			}
		}
		return container;
	}

	/**
	 * Attempts to insert this item's multipart into the container specified by the given context.
	 *
	 * @param context The multipart placement context.
	 * @return If the placement succeeded.
	 */
	protected boolean tryPlace(MultipartPlacementContext context) {
		MultipartState placementState = part.getPlacementState(context);

		if (context.getContainer().canInsert(placementState)) {
			context.getContainer().insert(placementState);
			context.getStack().split(1);
			return true;
		}
		return false;
	}

}
