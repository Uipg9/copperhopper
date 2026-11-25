package dev.brian.copperhopper.client;

import dev.brian.copperhopper.Copperhopper;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

/**
 * In-game test: chest -> hopper -> chest, once with a copper hopper and once with a vanilla hopper,
 * started on the same tick. The copper lane must move items, but about half as many.
 * Run with {@code .\gradlew.bat runClientGameTest}; screenshots land in
 * {@code build\run\clientGameTest\screenshots}.
 */
public class CopperhopperClientGameTest implements FabricClientGameTest {
	private static final BlockPos COPPER_OUT = new BlockPos(2, 101, 6);
	private static final BlockPos VANILLA_OUT = new BlockPos(5, 101, 6);
	private static final BlockPos PICKUP_HOPPER = new BlockPos(8, 101, 6);
	private static final int RUN_TICKS = 160;

	@Override
	public void runTest(ClientGameTestContext context) {
		if (!FabricLoader.getInstance().isModLoaded(Copperhopper.MOD_ID)) {
			throw new AssertionError(Copperhopper.MOD_ID + " is not loaded");
		}
		context.waitForScreen(TitleScreen.class);
		context.waitTicks(60);

		try (TestSingleplayerContext world = context.worldBuilder().create()) {
			context.waitTicks(100);

			// registration + recipe (5 copper ingots + chest)
			int ingredients = world.getServer().computeOnServer(server -> {
				var holder = server.getRecipeManager()
					.byKey(ResourceKey.create(Registries.RECIPE, Copperhopper.id("copper_hopper")))
					.orElseThrow(() -> new AssertionError("recipe copperhopper:copper_hopper missing"));
				return (int) ((ShapedRecipe) holder.value()).getIngredients().stream().filter(java.util.Optional::isPresent).count();
			});
			expect(ingredients == 6, "recipe should have 6 ingredients, had " + ingredients);

			world.getServer().runCommand("fill 0 100 0 10 100 10 stone");
			world.getServer().runCommand("fill 0 101 0 10 106 10 air");
			world.getServer().runCommand("tp @p 5.5 102 1.5 0 30");
			buildLane(world, COPPER_OUT, "copperhopper:copper_hopper");
			buildLane(world, VANILLA_OUT, "minecraft:hopper");
			// pickup check: a copper hopper with nothing above, an item entity dropped onto it
			world.getServer().runCommand("setblock 8 101 6 copperhopper:copper_hopper[facing=north]");
			context.waitTicks(2);
			expect(world.getServer().computeOnServer(server ->
				server.overworld().getBlockEntity(COPPER_OUT.above()) instanceof HopperBlockEntity),
				"copper hopper should have a vanilla hopper block entity");

			// start both lanes on the same tick
			world.getServer().runOnServer(server -> {
				server.getCommands().performPrefixedCommand(server.createCommandSourceStack(),
					"item replace block 2 103 6 container.0 with minecraft:cobblestone 64");
				server.getCommands().performPrefixedCommand(server.createCommandSourceStack(),
					"item replace block 5 103 6 container.0 with minecraft:cobblestone 64");
				server.getCommands().performPrefixedCommand(server.createCommandSourceStack(),
					"summon item 8.5 102.2 6.5 {Item:{id:\"minecraft:diamond\",count:1}}");
			});
			context.waitTicks(RUN_TICKS);
			context.takeScreenshot("hopper_lanes");

			int copper = count(world, COPPER_OUT);
			int vanilla = count(world, VANILLA_OUT);
			System.out.println("[copperhopper] after " + RUN_TICKS + " ticks: copper moved " + copper + ", vanilla moved " + vanilla);
			expect(copper > 0, "copper hopper moved nothing");
			expect(vanilla > copper, "copper (" + copper + ") should be slower than vanilla (" + vanilla + ")");
			expect(Math.abs(copper * 2 - vanilla) <= 3, "copper (" + copper + ") should be about half of vanilla (" + vanilla + ")");
			expect(count(world, PICKUP_HOPPER) == 1, "copper hopper should pick up the dropped diamond");
		}
	}

	/** Chest at y+2, hopper facing down at y+1, output chest at {@code out}. */
	private static void buildLane(TestSingleplayerContext world, BlockPos out, String hopper) {
		world.getServer().runCommand("setblock " + out.getX() + " " + out.getY() + " " + out.getZ() + " chest");
		world.getServer().runCommand("setblock " + out.getX() + " " + (out.getY() + 1) + " " + out.getZ() + " " + hopper + "[facing=down]");
		world.getServer().runCommand("setblock " + out.getX() + " " + (out.getY() + 2) + " " + out.getZ() + " chest");
	}

	private static int count(TestSingleplayerContext world, BlockPos pos) {
		return world.getServer().computeOnServer(server -> {
			if (!(server.overworld().getBlockEntity(pos) instanceof Container container)) {
				throw new AssertionError("no container at " + pos);
			}
			int total = 0;
			for (int i = 0; i < container.getContainerSize(); i++) {
				total += container.getItem(i).getCount();
			}
			return total;
		});
	}

	private static void expect(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}
}
