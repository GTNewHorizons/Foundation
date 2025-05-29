package com.gtnewhorizons.foundation.mixins.early;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.foundation.Foundation;
import com.gtnewhorizons.foundation.HandlerRegistry;

@Mixin(S21PacketChunkData.class)
public class MixinS21PacketChunkData {

    @Shadow
    private static byte[] field_149286_i = null; // Byte array for entire chunk data

    /**
     * @author Cleptomania
     * @reason If something is targeting this, it is guaranteed not gonna work with Foundation
     */
    @Overwrite
    public static int func_149275_c() {
        return HandlerRegistry.getChunkPacketBytes();
    }

    /**
     * @author Cleptomania
     * @reason If something is targeting this, it is guaranteed not gonna work with Foundation
     */
    @Overwrite
    public static S21PacketChunkData.Extracted func_149269_a(Chunk chunk, boolean sendUpdates, int flagSubChunks) {
        if (field_149286_i == null) {
            field_149286_i = new byte[HandlerRegistry.getChunkPacketBytes()];
        }

        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        S21PacketChunkData.Extracted extracted = new S21PacketChunkData.Extracted();

        if (sendUpdates) {
            chunk.sendUpdates = true;
        }

        // This field gets used in S26PacketMapChunkBulk so we still need to set it here
        // This is a mask for the LSB arrays, which isn't really needed and Minecraft doesn't
        // even really seem to handle it properly, but S26 needs it to function properly
        for (int i = 0; i < aextendedblockstorage.length; ++i) {
            if (aextendedblockstorage[i] != null && (!sendUpdates || !aextendedblockstorage[i].isEmpty())
                && (flagSubChunks & 1 << i) != 0) {
                extracted.field_150280_b |= 1 << i;
            }
        }

        int bytes = HandlerRegistry.writeChunkPackets(chunk, sendUpdates, flagSubChunks, field_149286_i);

        extracted.field_150282_a = new byte[bytes];
        System.arraycopy(field_149286_i, 0, extracted.field_150282_a, 0, bytes);
        return extracted;
    }
}
