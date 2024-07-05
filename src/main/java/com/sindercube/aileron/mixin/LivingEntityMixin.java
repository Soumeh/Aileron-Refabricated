package com.sindercube.aileron.mixin;

import com.sindercube.aileron.registry.AileronAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 6))
	private void modifyVelocity(LivingEntity entity, Vec3d vec3d) {
		Vec3d negator = new Vec3d(1.0 / 0.9900000095367432D, 1.0, 1.0 / 0.9900000095367432D);

		int cloudSkipper = (int)entity.getAttributeValue(AileronAttributes.CLOUD_DRAG_REDUCTION);

		double fac = 0;
		double y = entity.getY();
		if (y < 100)
			fac = 0.0;
		else if (y < 230)
			fac = 0.00006 * Math.pow(y - 100, 2);
		else
			fac = 1;

		fac *= 0.6;
		fac *= cloudSkipper / 3.0;

		if (fac > 0.1 && !getWorld().isClient && age % ((int) (1.0 - fac) * 2 + 1) == 0) {
			ServerWorld world = (ServerWorld)getWorld();

			for (ServerPlayerEntity player : world.getPlayers()) {
				Vec3d pos = entity.getPos().add(entity.getRotationVector().multiply(-1.0));
				world.spawnParticles(player, ParticleTypes.POOF, false, pos.x, pos.y, pos.z, 1 + (int) (fac * 4.0), 0.1, 0.1, 0.1, 0.025);
			}
		}

		negator = new Vec3d(negator.x, 1.0, negator.z);

		// lerp between vec3 and vec3 * negator based on fac
		vec3d = vec3d.lerp(vec3d.multiply(negator), fac);

		entity.setVelocity(vec3d);
	}
}
