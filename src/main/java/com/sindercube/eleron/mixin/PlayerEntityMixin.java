package com.sindercube.eleron.mixin;

import com.sindercube.eleron.access.EleronPlayerEntity;
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
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements EleronPlayerEntity {

	@SuppressWarnings("all")
	@Unique private static final TrackedData<Integer> SMOKE_STACKS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique private int smokeStackDashCooldown = 0;
	@Unique private int smokeTrailTicks = 0;
	@Unique private int campfireChargeTime = 0;
	@Unique private int flightBoostTicks = 0;
	@Unique private int flyingTimer = 0;

	@Inject(method = "tick", at = @At("TAIL"))
	public void postTick(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity)(Object)this;
		World world = self.getWorld();

		if (smokeStackDashCooldown > 0) smokeStackDashCooldown--;
		if (smokeTrailTicks > 0) smokeTrailTicks--;
		if (flightBoostTicks > 0) flightBoostTicks--;
		if (flyingTimer > 0) flyingTimer--;

		EntityHandler.tickSmokeTrial(world, self);
		EntityHandler.tickFlyingTimer(world, self);
		EntityHandler.tickFlightBoost(world, self);
		EntityHandler.tickCampfireUpdrafts(world, self);
	}


	@Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
	private static void addDefaultAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		DefaultAttributeContainer.Builder builder = cir.getReturnValue();
		builder = builder
				.add(EleronAttributes.MAX_SMOKESTACKS, 0)
				.add(EleronAttributes.ALTITUDE_DRAG_REDUCTION, 0);
		cir.setReturnValue(builder);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void addCustomData(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(SMOKE_STACKS, 0);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomData(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("smoke_stacks"))
			setSmokeStacks(nbt.getInt("smoke_stacks"));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomData(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("smoke_stacks", getSmokeStacks());
	}


	@Override
	public int getMaxSmokeStacks() {
		return (int) getAttributeValue(EleronAttributes.MAX_SMOKESTACKS);
	}

	@Override
	public int getSmokeStacks() {
		return dataTracker.get(SMOKE_STACKS);
	}
	@Override
	public void setSmokeStacks(int stacks) {
		dataTracker.set(SMOKE_STACKS, stacks);
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

	@Override
	public int getFlyingTimer() {
		return flyingTimer;
	}
	@Override
	public void setFlyingTimer(int timer) {
		this.flyingTimer = timer;
	}

}
