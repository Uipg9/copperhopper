package dev.brian.copperhopper.mixin;

import dev.brian.copperhopper.CooldownRule;
import dev.brian.copperhopper.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Copper hoppers wait twice as long between transfers; every other hopper is untouched. */
@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@ModifyVariable(method = "setCooldown", at = @At("HEAD"), argsOnly = true)
	private int copperhopper$slowCooldown(int cooldown) {
		if (((BlockEntity) (Object) this).getBlockState().is(ModBlocks.COPPER_HOPPER)) {
			return CooldownRule.copperCooldown(cooldown);
		}
		return cooldown;
	}
}
