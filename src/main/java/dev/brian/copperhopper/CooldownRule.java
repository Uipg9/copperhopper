package dev.brian.copperhopper;

/** Pure rule: the copper hopper waits twice as long as a vanilla hopper between transfers. */
public final class CooldownRule {
	public static final int VANILLA_COOLDOWN = 8;
	public static final int COPPER_COOLDOWN = 16;

	private CooldownRule() {
	}

	/** Maps a cooldown vanilla would set to the copper one. Reset values (<= 0) pass through. */
	public static int copperCooldown(int vanillaCooldown) {
		if (vanillaCooldown <= 0) {
			return vanillaCooldown;
		}
		return vanillaCooldown * COPPER_COOLDOWN / VANILLA_COOLDOWN;
	}
}
