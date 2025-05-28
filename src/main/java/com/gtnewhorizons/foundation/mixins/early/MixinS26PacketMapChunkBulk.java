package com.gtnewhorizons.foundation.mixins.early;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(S26PacketMapChunkBulk.class)
public class MixinS26PacketMapChunkBulk {

    @Shadow
    private byte[][] field_149260_f;

    private int[] s21Sizes;

    @Inject(
        method = "readPacketData",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readInt()I", ordinal = 0))
    private void foundation$injectS21SizeReads(PacketBuffer data, CallbackInfo ci, @Local(ordinal = 0) short chunks) {
        s21Sizes = new int[chunks];
        for (int i = 0; i < chunks; i++) {
            s21Sizes[i] = data.readInt();
        }
    }

    // TODO: Convert these all to one mixin applied on a slice, this is easier and I'm lazy
    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 5), ordinal = 4)
    private int foundation$modifyI1Value1(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j];
    }

    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 6), ordinal = 4)
    private int foundation$modifyI1Value2(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j];
    }

    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 7), ordinal = 4)
    private int foundation$modifyI1Value3(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j];
    }

    @Inject(
        method = "writePacketData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketBuffer;writeInt(I)Lio/netty/buffer/ByteBuf;",
            ordinal = 0))
    private void foundation$injectSizeWrites(PacketBuffer data, CallbackInfo ci) {
        for (int i = 0; i < field_149260_f.length; i++) {
            data.writeInt(field_149260_f[i].length);
        }
    }

}
