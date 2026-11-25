package dev.brian.copperhopper.mixin;

import dev.brian.copperhopper.CooldownRule;
import dev.brian.copperhopper.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.chat.Component;

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

	/** The menu title says "Copper Hopper" instead of vanilla's "Hopper" (a renamed hopper keeps its name). */
	@Inject(method = "getDefaultName", at = @At("HEAD"), cancellable = true)
	private void copperhopper$copperName(CallbackInfoReturnable<Component> cir) {
		if (((BlockEntity) (Object) this).getBlockState().is(ModBlocks.COPPER_HOPPER)) {
			cir.setReturnValue(Component.translatable("container.copperhopper.copper_hopper"));
		}
	}
}
