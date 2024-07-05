package com.sindercube.aileron.mixin;

import com.sindercube.aileron.access.AileronPlayerEntity;
import com.sindercube.aileron.registry.AileronAttachments;
import com.sindercube.aileron.registry.AileronAttributes;
import com.sindercube.aileron.registry.AileronGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

	@Unique int campfireDamageIFrames = 0;
	@Unique int smokeTrailTicks = 0;
	@Unique int chargeTime = 0;
	@Unique boolean charged = false;
	@Unique int startFlyingTimer = 0;
	@Unique int boostTicks = 0;

	@Shadow public abstract boolean isMainPlayer();

	@Inject(method = "tick", at = @At("TAIL"))
	public void postTick(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object)this;
		World world = self.getWorld();

		if (boostTicks > 0) boostTicks--;
		if (!self.isFallFlying()) boostTicks = 0;

		if (campfireDamageIFrames > 0) campfireDamageIFrames--;
		if (startFlyingTimer > 0) startFlyingTimer--;

		if (smokeTrailTicks > 0) smokeTrailTicks--;
		if (!self.isFallFlying()) smokeTrailTicks = 0;


		// boosting
		if (boostTicks > 0) {

			if (world.isClient && isMainPlayer()) {
				Vec3d vec31 = self.getRotationVector();
				Vec3d vec32 = self.getVelocity();
				self.setVelocity(vec32.add(vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D));
			} else {
				if (self.age % 3 == 0) {

					final ServerWorld serverWorld = ((ServerWorld) world);

					for (ServerPlayerEntity player : serverWorld.getPlayers()) {
						serverWorld.spawnParticles(player, ParticleTypes.FLAME, false, self.getX(), self.getY(), self.getZ(), 2, 0.2, 0.2, 0.2, 0.1);
						serverWorld.spawnParticles(player, ParticleTypes.LARGE_SMOKE, false, self.getX(), self.getY(), self.getZ(), 3, 0.2, 0.2, 0.2, 0.1);
						serverWorld.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, false, self.getX(), self.getY(), self.getZ(), 1, 0.2, 0.2, 0.2, 0.0);
					}


				}
			}

		}

		// smoke trail
		if (smokeTrailTicks > 0) {

			if (self.age % 3 == 0) {
				final ServerWorld serverWorld = ((ServerWorld) world);

				for (ServerPlayerEntity player : serverWorld.getPlayers()) {
					Vec3d pos = self.getPos().add(self.getRotationVector().multiply(-1.0));
					serverWorld.spawnParticles(player, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, false, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.005);
				}
			}

		}

		BlockState underBlockState = world.getBlockState(self.getBlockPos());
		if (self.isSneaking() && underBlockState.isIn(BlockTags.CAMPFIRES) && self.getInventory().getArmorStack(2).getItem() instanceof ElytraItem) {

			if (world.isClient) {
				SimpleParticleType[] particles = {ParticleTypes.FLAME, ParticleTypes.SMOKE};

				for (SimpleParticleType particle : particles) {
					Vec3d randomOffset = new Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
					randomOffset = randomOffset.normalize().multiply(2.0);
					Vec3d motion = randomOffset.multiply(-0.075);
					Vec3d position = self.getPos().add(0.0, self.getStandingEyeHeight() / 2.0, 0.0).add(randomOffset);
					world.addParticle(particle, position.x, position.y, position.z, motion.x, motion.y, motion.z);
				}
			} else {
				final ServerWorld serverWorld = ((ServerWorld) world);
				chargeTime++;

				if (chargeTime % world.getGameRules().getInt(AileronGamerules.SMOKE_STACK_CHARGE_TICKS) == 0 && chargeTime > 0) {
					int stocks = self.getSmokeStacks();
					int smokeStockMaxLevel = self.getMaxSmokestacks();

					if (stocks < smokeStockMaxLevel || !charged) {
						charged = true;

						if (stocks < smokeStockMaxLevel)
							self.modifySmokeStacks(stacks -> stacks + 1);


						for (ServerPlayerEntity player : serverWorld.getPlayers()) {
							serverWorld.spawnParticles(player, ParticleTypes.LARGE_SMOKE, false, self.getX(), self.getY(), self.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
							serverWorld.spawnParticles(player, ParticleTypes.SMOKE, false, self.getX(), self.getY(), self.getZ(), 100, 0.5, 0.5, 0.5, 0.4);
						}
						world.playSound(null, self.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.8f, 0.8f + (stocks * 0.2f));
					}
				}
			}
		} else {
			chargeTime = 0;

			if (!world.isClient && !self.isFallFlying() && campfireDamageIFrames == 0 && !charged) {
				self.setAttached(AileronAttachments.SMOKESTACKS, 0);
			}
		}

		if (startFlyingTimer == 0 && !world.isClient) {
			startFlyingTimer = -1;
			self.startFallFlying();
		}

		if (!self.isSneaking() && charged) {
			final ServerWorld serverWorld = ((ServerWorld) world);

			charged = false;
			chargeTime = 0;

			for (ServerPlayerEntity player : serverWorld.getPlayers()) {
				serverWorld.spawnParticles(player, ParticleTypes.LARGE_SMOKE, false, self.getY(), self.getY(), self.getZ(), 40, 0.5, 0.5, 0.5, 0.1);
				serverWorld.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, false, self.getY(), self.getY(), self.getZ(), 40, 0.5, 0.5, 0.5, 0.1);
				serverWorld.spawnParticles(player, ParticleTypes.SMOKE, false, self.getY(), self.getY(), self.getZ(), 120, 0.5, 0.5, 0.5, 0.4);
			}

			setCampfireDamageIFrames(20);

			world.playSound(null, self.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.8f, 0.8f);

			self.startFallFlying();
			// TODO do
//			AileronNetworking.sendSmokeStackLaunch((ServerPlayer) self);
			self.checkFallFlying();
			startFlyingTimer = 5;
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
				// player is over a campfire
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

					// if player is within range of campfire
					if (distance < range) {
						double force = Math.min(range / distance / 7, 1.0);


						Vec3d existingDeltaMovement = self.getVelocity();
						self.setVelocity(existingDeltaMovement.x, Math.min(existingDeltaMovement.y + force, 1.0), existingDeltaMovement.z);
					}
				}
			}

		}
		// TODO do this too
//		if (self.getWorld().isClient && self.isMainPlayer())
//			AileronClient.localPlayerTick(self);
	}


	@Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
	private static void addDefaultAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		DefaultAttributeContainer.Builder builder = cir.getReturnValue();
		builder = builder
				.add(AileronAttributes.SMOKESTACK_CHARGES, 0)
				.add(AileronAttributes.CLOUD_DRAG_REDUCTION, 0);
		cir.setReturnValue(builder);
	}

	@Override
	public int getMaxSmokestacks() {
		return (int)this.getAttributeValue(AileronAttributes.SMOKESTACK_CHARGES);
	}

	@Override
	public int getSmokeStacks() {
		return this.getAttachedOrCreate(AileronAttachments.SMOKESTACKS, this::getMaxSmokestacks);
	}

	@Override
	public void setSmokeStacks(int stacks) {
		this.setAttached(AileronAttachments.SMOKESTACKS, stacks);
	}


	@Override
	public boolean charged() {
		return charged;
	}

	@Override
	public int getBoostTicks() {
		return boostTicks;
	}

	@Override
	public void setBoostTicks(int boostTicks) {
		this.boostTicks = boostTicks;
	}

	@Override
	public void setSmokeTrailTicks(int boostTicks) {
		this.smokeTrailTicks = boostTicks;
	}

	@Override
	public int getCampfireDamageIFrames() {
		return campfireDamageIFrames;
	}

	@Override
	public void setCampfireDamageIFrames(int campfireDamageIFrames) {
		this.campfireDamageIFrames = campfireDamageIFrames;
	}

}
