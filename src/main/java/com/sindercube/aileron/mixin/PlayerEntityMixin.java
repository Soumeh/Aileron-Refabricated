package com.sindercube.aileron.mixin;

import com.sindercube.aileron.access.AileronPlayerEntity;
import com.sindercube.aileron.registry.AileronEntityData;
import com.sindercube.aileron.registry.AileronAttributes;
import com.sindercube.aileron.registry.AileronGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements AileronPlayerEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique private int smokeStackDashCooldown = 0;
	@Unique private int campfireIFrames = 0;
	@Unique private int smokeTrailTicks = 0;
	@Unique private int campfireChargeTime = 0;
	@Unique private boolean campfireCharged = false;
	@Unique private int flyingTimer = 0;
	@Unique private int flightBoostTicks = 0;

	@Shadow public abstract boolean isMainPlayer();
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(method = "tick", at = @At("TAIL"))
	public void postTick(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object)this;
		World world = self.getWorld();

		if (campfireIFrames > 0) campfireIFrames--;
		if (smokeStackDashCooldown > 0) smokeStackDashCooldown--;

		tickFlightBoost(world, self);
		tickSmokeTrial(world, self);

		if (flyingTimer > 0) flyingTimer--;
		if (flyingTimer == 0 && !world.isClient) {
			flyingTimer = -1;
			self.startFallFlying();
		}

		if (self.isFallFlying() && self.getWorld().isClient && self.isMainPlayer()) {
			int maxRange = 38;
			int depth = 0;

			BlockPos blockPosition = self.getBlockPos();

			while (depth < maxRange && world.isAir(blockPosition) && world.isInBuildLimit(blockPosition)) {
				depth++;
				blockPosition = blockPosition.down();
			}

			BlockState state = world.getBlockState(blockPosition);
			if (state.isIn(BlockTags.CAMPFIRES) && world.getGameRules().getBoolean(AileronGamerules.CAMPFIRES_PUSH_PLAYERS)) {
				// serverPlayer is over a campfire
				// determine range of campfire
				boolean isLit = state.get(CampfireBlock.LIT);


				if (isLit) {
					// check for neighboring campfires
					BlockPos[] possibleNeighbors = new BlockPos[]{
							blockPosition.north(),
							blockPosition.south(),
							blockPosition.east(),
							blockPosition.west()
					};

					int neighbors = 0;

					for (BlockPos neighbor : possibleNeighbors) {
						if (world.getBlockState(neighbor).getBlock() instanceof CampfireBlock) {
							neighbors++;
						}
					}


					boolean isExtendedRange = state.get(CampfireBlock.SIGNAL_FIRE);
					int range = isExtendedRange ? 24 : 10 + (int) (3.0 * neighbors);

					double distance = Math.abs(blockPosition.getY() - self.getY());

					// if serverPlayer is within range of campfire
					if (distance < range) {
						double force = Math.min(range / distance / 7, 1.0);


						Vec3d existingDeltaMovement = self.getVelocity();
						self.setVelocity(existingDeltaMovement.x, Math.min(existingDeltaMovement.y + force, 1.0), existingDeltaMovement.z);
					}
				}
			}

		}

	}


	@Unique
	public void tickFlightBoost(World world, PlayerEntity player) {
		if (flightBoostTicks > 0) flightBoostTicks--;
		else return;
		if (!player.isFallFlying()) flightBoostTicks = 0;

		System.out.println(flightBoostTicks);

		if (world.isClient && isMainPlayer()) {
			Vec3d rotation = player.getRotationVector();
			Vec3d velocity = player.getVelocity();
			velocity = velocity.add(
					rotation.x * 0.1D + (rotation.x * 1.5D - velocity.x) * 0.5D,
					rotation.y * 0.1D + (rotation.y * 1.5D - velocity.y) * 0.5D,
					rotation.z * 0.1D + (rotation.z * 1.5D - velocity.z) * 0.5D
			);
			player.setVelocity(velocity);
		} else if (player.age % 3 == 0) {
			ServerWorld serverWorld = (ServerWorld) world;
			Vec3d pos = player.getPos();
			serverWorld.spawnParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 2, 0.2, 0.2, 0.2, 0.1);
			serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 3, 0.2, 0.2, 0.2, 0.1);
			serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 1, 0.2, 0.2, 0.2, 0.0);
		}
	}

	@Unique
	private void tickSmokeTrial(World world, PlayerEntity player) {
		if (smokeTrailTicks > 0) smokeTrailTicks--;
		if (!player.isFallFlying()) smokeTrailTicks = 0;

		if (smokeTrailTicks <= 0) return;

		if (player.age % 3 == 0) {
			final ServerWorld serverWorld = (ServerWorld) world;
			Vec3d pos = player.getPos().add(player.getRotationVector().multiply(-1.0));
			serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.005);
		}
	}


	@Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
	private static void addDefaultAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		DefaultAttributeContainer.Builder builder = cir.getReturnValue();
		builder = builder
				.add(AileronAttributes.MAX_SMOKESTACKS, 0)
				.add(AileronAttributes.ALTITUDE_DRAG_REDUCTION, 0);
		cir.setReturnValue(builder);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void addEntityData(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(AileronEntityData.SMOKE_STACK_CHARGES, 0);
	}


	@Override
	public int getMaxSmokeStacks() {
		return (int)this.getAttributeValue(AileronAttributes.MAX_SMOKESTACKS);
	}

	@Override
	public int getSmokeStacks() {
		return this.getDataTracker().get(AileronEntityData.SMOKE_STACK_CHARGES);
	}
	@Override
	public void setSmokeStacks(int stacks) {
		this.getDataTracker().set(AileronEntityData.SMOKE_STACK_CHARGES, stacks);
	}

	@Override
	public int getSmokeStackDashCooldown() {
		return smokeStackDashCooldown;
	}
	@Override
	public void setSmokeStackDashCooldown(int cooldown) {
		this.smokeStackDashCooldown = cooldown;
	}

	@Override
	public boolean isCampfireCharged() {
		return campfireCharged;
	}
	@Override
	public void setCampfireCharged(boolean charged) {
		this.campfireCharged = charged;
	}

	@Override
	public int getCampfireChargeTime() {
		return campfireChargeTime;
	}
	@Override
	public void setCampfireChargeTime(int time) {
		this.campfireChargeTime = time;
	}

	@Override
	public void setFlightBoostTicks(int ticks) {
		this.flightBoostTicks = ticks;
	}
	@Override
	public void setSmokeTrailTicks(int boostTicks) {
		this.smokeTrailTicks = boostTicks;
	}
	@Override
	public void setFlyingTimer(int timer) {
		this.flyingTimer = timer;
	}

	@Override
	public int getCampfireIFrames() {
		return campfireIFrames;
	}
	@Override
	public void setCampfireIFrames(int frames) {
		this.campfireIFrames = frames;
	}

}
