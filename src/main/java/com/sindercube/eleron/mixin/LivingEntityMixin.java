package com.sindercube.eleron.mixin;

import com.sindercube.eleron.handlers.EntityHandler;
import com.sindercube.eleron.registry.EleronGamerules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	protected abstract Vec3d calcGlidingVelocity(Vec3d velocity);

	@Shadow
	public abstract void remove(RemovalReason reason);

	@Shadow
	protected abstract boolean canGlide();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "travelGliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void modifyVelocity(LivingEntity entity, Vec3d velocity) {
		velocity = this.calcGlidingVelocity(velocity);
		EntityHandler.cloudskipperVelocityModifier(entity, velocity);
	}


	@Unique private int ticksOnGround = 0;

	@ModifyArg(method = "tickGliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
	private boolean updateGliding(boolean canGlide) {
		if (!(this.getWorld() instanceof ServerWorld world)) return canGlide;
		if (!world.getGameRules().getBoolean(EleronGamerules.DO_ELYTRA_BOUNCE)) return canGlide;

		if (ticksOnGround < 3) return true;
		ticksOnGround = 0;
		return canGlide;
	}

	@Inject(method = "tickGliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canGlide()Z"))
	private void updateCanGlide(CallbackInfo ci) {
		if (!(this.getWorld() instanceof ServerWorld world)) return;
		if (!world.getGameRules().getBoolean(EleronGamerules.DO_ELYTRA_BOUNCE)) return;

		if (this.canGlide()) {
			ticksOnGround = 0;
		} else {
			ticksOnGround = Math.max(2, ++ticksOnGround);
		}
	}

//	@Inject(method = "tickGliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V", shift = At.Shift.AFTER))
//	private void updateFallFlying(CallbackInfo ci, @Local boolean flag) {
//		World world = this.getWorld();
//		if (!(world instanceof ServerWorld serverWorld)) return;
//		if (serverWorld.getGameRules().getBoolean(EleronGamerules.DO_ELYTRA_BOUNCE) && wasGoodBefore && !flag && this.canGlide()) {
//			ticksOnGround++;
//			wasGoodBefore = true;
//			this.setFlag(7, true);
//		} else {
//			ticksOnGround = 0;
//			wasGoodBefore = flag;
//		}
//	}

}
