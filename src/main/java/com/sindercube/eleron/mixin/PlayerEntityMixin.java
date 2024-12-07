package com.sindercube.eleron.mixin;

import com.sindercube.eleron.inject.EleronPlayerEntity;
import com.sindercube.eleron.handlers.EntityHandler;
import com.sindercube.eleron.registry.EleronAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements EleronPlayerEntity {

	@Unique private static final TrackedData<Integer> SMOKESTACK_CHARGES = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique private int smokeStackDashCooldown = 0;
	@Unique private int smokeTrailTicks = 0;
	@Unique private int campfireChargeTime = 0;
	@Unique private int flightBoostTicks = 0;

	@Inject(method = "tick", at = @At("TAIL"))
	public void postTick(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object)this;
		World world = self.getWorld();

		if (smokeStackDashCooldown > 0) smokeStackDashCooldown--;
		if (smokeTrailTicks > 0) smokeTrailTicks--;
		if (flightBoostTicks > 0) flightBoostTicks--;

		EntityHandler.tickSmokeTrial(world, self);
		EntityHandler.tickSmokestackChargeDecay(world, self);
		EntityHandler.tickFlightBoost(world, self);

		if (world instanceof ServerWorld serverWorld) {
			EntityHandler.tickCampfireCharging(serverWorld, self);
			EntityHandler.tickCampfireUpdrafts(serverWorld, self);
		}
	}


	@Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
	private static void addDefaultAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		DefaultAttributeContainer.Builder builder = cir.getReturnValue();
		builder = builder
				.add(EleronAttributes.MAX_SMOKESTACK_CHARGES, 0)
				.add(EleronAttributes.ALTITUDE_DRAG_REDUCTION, 0);
		cir.setReturnValue(builder);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void addCustomData(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(SMOKESTACK_CHARGES, 0);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomData(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("smokestack_charges"))
			setSmokestackCharges(nbt.getInt("smokestack_charges"));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomData(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("smokestack_charges", getSmokestackCharges());
	}


	@Override
	public int getMaxSmokestackCharges() {
		return (int) getAttributeValue(EleronAttributes.MAX_SMOKESTACK_CHARGES);
	}

	@Override
	public int getSmokestackCharges() {
		return dataTracker.get(SMOKESTACK_CHARGES);
	}
	@Override
	public void setSmokestackCharges(int stacks) {
		dataTracker.set(SMOKESTACK_CHARGES, stacks);
	}

	@Override
	public int getSmokestackChargeCooldown() {
		return smokeStackDashCooldown;
	}
	@Override
	public void setSmokestackChargeCooldown(int cooldown) {
		this.smokeStackDashCooldown = cooldown;
	}

	@Override
	public int getSmokeTrailTicks() {
		return smokeTrailTicks;
	}
	@Override
	public void setSmokeTrailTicks(int boostTicks) {
		this.smokeTrailTicks = boostTicks;
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
	public int getFlightBoostTicks() {
		return flightBoostTicks;
	}
	@Override
	public void setFlightBoostTicks(int ticks) {
		this.flightBoostTicks = ticks;
	}

}
