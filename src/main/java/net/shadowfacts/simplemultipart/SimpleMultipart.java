package net.shadowfacts.simplemultipart;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.loot.context.LootContextParameter;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;
import net.shadowfacts.simplemultipart.container.*;
import net.shadowfacts.simplemultipart.item.MultipartItem;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.test.*;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author shadowfacts
 */
public class SimpleMultipart implements ModInitializer {

	public static final String MODID = "simplemultipart";

	public static final Registry<Multipart> MULTIPART = createMultipartRegistry();

	public static final LootContextParameter<MultipartState> MULTIPART_STATE_PARAMETER = new LootContextParameter<>(new Identifier(MODID, "multipart_state"));
	public static final LootContextType MULTIPART_LOOT_CONTEXT = createMultipartLootContextType();

	public static final ContainerBlock containerBlock = new ContainerBlock();
	public static final TickableContainerBlock tickableContainerBlock = new TickableContainerBlock();
	public static final BlockEntityType<ContainerBlockEntity> containerBlockEntity = createBlockEntityType("container", ContainerBlockEntity::new);
	public static final BlockEntityType<TickableContainerBlockEntity> tickableContainerBlockEntity = createBlockEntityType("tickable_container", TickableContainerBlockEntity::new);

	private static BiFunction<String, Consumer<LootContextType.Builder>, LootContextType> lootContextRegisterFunction;


	// TEST ITEMS
	public static final TestMultipart testPart = new TestMultipart();
	public static final SlabMultipart ironSlab = new SlabMultipart();
	public static final SlabMultipart goldSlab = new SlabMultipart();
	public static final EntityTestPart entityTest = new EntityTestPart();
	public static final TickableEntityTestPart tickableEntityTest = new TickableEntityTestPart();

	public static final MultipartItem testItem = new MultipartItem(testPart);
	public static final MultipartItem ironSlabItem = new MultipartItem(ironSlab);
	public static final MultipartItem goldSlabItem  = new MultipartItem(goldSlab);
	public static final MultipartItem entityTestItem = new MultipartItem(entityTest);
	public static final MultipartItem tickableEntityTestItem = new MultipartItem(tickableEntityTest);


	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier(MODID, "container"), containerBlock);
		Registry.register(Registry.BLOCK, new Identifier(MODID, "tickable_container"), tickableContainerBlock);

		ContainerEventHandler.register();

		//TEST REGISTRY
		registerPartAndItem("test_part", testPart, testItem);
		registerPartAndItem("iron_slab", ironSlab, ironSlabItem);
		registerPartAndItem("gold_slab", goldSlab, goldSlabItem);
		registerPartAndItem("entity_test", entityTest, entityTestItem);
		registerPartAndItem("tickable_entity_test", tickableEntityTest, tickableEntityTestItem);
	}


	//TEST FUNCTION
	private void registerPartAndItem(String name, Multipart part, Item item) {
		Identifier id = new Identifier(MODID, name);
		Registry.register(SimpleMultipart.MULTIPART, id, part);
		Registry.register(Registry.ITEM, id, item);
	}

	private static Registry<Multipart> createMultipartRegistry() {
		SimpleRegistry<Multipart> registry = new SimpleRegistry<>();
		Registry.REGISTRIES.add(new Identifier(MODID, "multipart"), registry);
		return registry;
	}

	private static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(String name, Supplier<T> supplier) {
		BlockEntityType.Builder<T> builder = BlockEntityType.Builder.create(supplier);
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(MODID, name), builder.build(null));
	}

	@Deprecated
	public static void setLootContextRegisterFunction(BiFunction<String, Consumer<LootContextType.Builder>, LootContextType> function) {
		lootContextRegisterFunction = function;
	}

	private static LootContextType createMultipartLootContextType() {
		try {
			// Load the function
			Class.forName(LootContextTypes.class.getCanonicalName());
		} catch (ClassNotFoundException e) {}
		return lootContextRegisterFunction.apply(
				MODID + ":multipart",
				builder -> builder.require(MULTIPART_STATE_PARAMETER).require(LootContextParameters.POSITION)
		);
	}

}
