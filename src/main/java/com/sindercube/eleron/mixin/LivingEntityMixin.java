package com.sindercube.eleron.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.sindercube.eleron.handlers.EntityHandler;
import com.sindercube.eleron.registry.EleronGamerules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 6))
	private void modifyVelocity(LivingEntity entity, Vec3d velocity) {
		EntityHandler.cloudskipperVelocityModifier(entity, velocity);
	}


	@Unique private int ticksOnGround = 0;
	@Unique private boolean wasGoodBefore = false;

	@ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"), index = 1)
	private boolean updateTravel(boolean isOnGround) {
		if (ticksOnGround <= 2) return true;
		return isOnGround;
	}

	@Inject(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V", shift = At.Shift.AFTER))
	private void updateFallFlying(CallbackInfo ci, @Local boolean flag) {
		if (getWorld().getGameRules().getBoolean(EleronGamerules.DO_ELYTRA_BOUNCE) && wasGoodBefore && !flag && this.isOnGround()) {
			ticksOnGround++;
			wasGoodBefore = true;
			this.setFlag(7, true);
		} else {
			ticksOnGround = 0;
			wasGoodBefore = flag;
		}
	}

}
