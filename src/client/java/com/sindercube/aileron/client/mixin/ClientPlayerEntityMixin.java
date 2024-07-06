package com.sindercube.aileron.client.mixin;

import com.sindercube.aileron.content.packets.SmokeStackLaunchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

//    @Unique private static int cooldown = 0;
//    @Unique private static boolean wasJumping = false;
//
//    @Inject(method = "tick", at = @At("TAIL"))
//    private void tickSmokestackDash(CallbackInfo ci) {
//        ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
//        if (cooldown > 0) cooldown--;
//
//        boolean jumping = self.input.jumping;
//        if (jumping && !wasJumping && cooldown <= 0 && self.isFallFlying() && self.getFallFlyingTicks() > 0) {
//            ClientPlayNetworking.send(new SmokeStackLaunchPacket());
//            cooldown = 50;
//        }
//
//        wasJumping = jumping;
//    }

}
