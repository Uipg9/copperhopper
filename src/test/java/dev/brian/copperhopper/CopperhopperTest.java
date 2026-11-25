package dev.brian.copperhopper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CopperhopperTest {
	@Test
	void idUsesModNamespace() {
		var id = Copperhopper.id("example");
		assertEquals(Copperhopper.MOD_ID, id.getNamespace());
		assertEquals("example", id.getPath());
	}

	@Test
	void langKeyFollowsMinecraftConvention() {
		assertEquals("item.copperhopper.example", Copperhopper.langKey("item", "example"));
	}
}
