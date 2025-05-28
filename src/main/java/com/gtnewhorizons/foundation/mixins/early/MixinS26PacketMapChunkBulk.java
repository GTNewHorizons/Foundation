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

    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 5), ordinal = 4 // This should target
                                                                                                  // i1
    )
    private int foundation$modifyI1Value1(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j]; // Or whatever size you want to use
    }

    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 6), ordinal = 4 // This should target
                                                                                                  // i1
    )
    private int foundation$modifyI1Value2(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j]; // Or whatever size you want to use
    }

    @ModifyVariable(method = "readPacketData", at = @At(value = "LOAD", ordinal = 7), ordinal = 4 // This should target
                                                                                                  // i1
    )
    private int foundation$modifyI1Value3(int original, @Local(ordinal = 1) int j) {
        return s21Sizes[j]; // Or whatever size you want to use
    }

    // @ModifyArg(
    // method = "readPacketData",
    // at = @At(
    // value = "NEW",
    // target = "([B)V",
    // ordinal = 1 // This targets the specific byte[] creation
    // )
    // )
    // private int foundation$modifyByteArraySize(int originalSize, @Local(ordinal = 1) int j) {
    // // Modify the size here
    // return s21Sizes[j]; // For example, doubles the size
    // }

    // @ModifyArg(
    // method = "readPacketData",
    // at = @At(
    // value = "INVOKE",
    // target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
    // ordinal = 1 // This targets the specific arraycopy call
    // ),
    // index = 4 // This targets the length parameter (5th argument)
    // )
    // private int foundation$modifyArrayCopyLength(int originalLength, @Local(ordinal = 1) int j) {
    // // Modify the copy length here
    // return s21Sizes[j]; // For example, doubles the length
    // }
    //
    // @ModifyVariable(
    // method = "readPacketData",
    // at = @At(
    // value = "INVOKE_ASSIGN",
    // target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
    // ordinal = 1
    // ),
    // ordinal = 0 // This targets the 'i' variable
    // )
    // private int foundation$modifyIValue(int originalValue, @Local(ordinal = 1) int j) {
    // // Modify i here
    // return s21Sizes[j]; // Or whatever value you want to use
    // }

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
