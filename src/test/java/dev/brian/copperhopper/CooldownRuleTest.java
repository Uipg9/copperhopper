package dev.brian.copperhopper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CooldownRuleTest {
	@Test
	void vanillaTransferCooldownIsDoubled() {
		assertEquals(16, CooldownRule.copperCooldown(8));
	}

	@Test
	void chainedHopperOffsetIsDoubledToo() {
		// Vanilla sets 8 - 1 = 7 on a receiving hopper that already ticked this game tick.
		assertEquals(14, CooldownRule.copperCooldown(7));
	}

	@Test
	void resetAndUnsetValuesPassThrough() {
		assertEquals(0, CooldownRule.copperCooldown(0));
		assertEquals(-1, CooldownRule.copperCooldown(-1));
	}
}
