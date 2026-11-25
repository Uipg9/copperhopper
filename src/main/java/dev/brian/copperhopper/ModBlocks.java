package dev.brian.copperhopper;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntityTypes;

/**
 * The copper hopper is a plain vanilla {@link HopperBlock} with the vanilla hopper block entity, so
 * menu, comparator, redstone lock, entity pickup and container/Transfer API interop are all vanilla.
 * Only the transfer cooldown differs (see {@code HopperBlockEntityMixin}).
 */
public final class ModBlocks {
	public static final ResourceKey<Block> COPPER_HOPPER_KEY = ResourceKey.create(Registries.BLOCK, Copperhopper.id("copper_hopper"));
	public static final Block COPPER_HOPPER = Registry.register(BuiltInRegistries.BLOCK, COPPER_HOPPER_KEY,
		new HopperBlock(Block.Properties.ofFullCopy(Blocks.HOPPER).setId(COPPER_HOPPER_KEY)));

	private static final ResourceKey<Item> ITEM_KEY = ResourceKey.create(Registries.ITEM, Copperhopper.id("copper_hopper"));
	public static final Item COPPER_HOPPER_ITEM = Registry.register(BuiltInRegistries.ITEM, ITEM_KEY,
		new BlockItem(COPPER_HOPPER, new Item.Properties().setId(ITEM_KEY).useBlockDescriptionPrefix()));

	private ModBlocks() {
	}

	public static void initialize() {
		// Let the vanilla hopper block entity type accept our block.
		((FabricBlockEntityType) BlockEntityTypes.HOPPER).addValidBlock(COPPER_HOPPER);

		ResourceKey<CreativeModeTab> redstone = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
			Identifier.withDefaultNamespace("redstone_blocks"));
		CreativeModeTabEvents.modifyOutputEvent(redstone).register(output -> output.insertAfter(Items.HOPPER, COPPER_HOPPER_ITEM));
	}
}
