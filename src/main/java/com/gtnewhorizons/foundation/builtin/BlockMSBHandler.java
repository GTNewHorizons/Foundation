package com.gtnewhorizons.foundation.builtin;

import java.nio.ByteBuffer;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class BlockMSBHandler implements ChunkPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return (2048 + 2) * 16; // (16 * 16 * 16 / 2) bytes per section * 16 sections + 2 bytes for flags
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        int start = buffer.position();
        buffer.position(start + 2); // Leave room for MSB flags

        int flagMsb = 0;
        // First pass to calculate MSB flags
        for (int i = 0; i < aextendedblockstorage.length; i++) {
            if (aextendedblockstorage[i] != null && (!sendUpdates || !aextendedblockstorage[i].isEmpty())
                && (flagSubChunks & 1 << i) != 0) {
                NibbleArray msb = aextendedblockstorage[i].getBlockMSBArray();
                if (msb != null) {
                    flagMsb |= 1 << i;
                }
            }
        }

        // Second pass to write MSB data
        for (int i = 0; i < aextendedblockstorage.length; i++) {
            if ((flagMsb & 1 << i) != 0 && aextendedblockstorage[i] != null) {
                buffer.put(aextendedblockstorage[i].getBlockMSBArray().data);
            }
        }

        int end = buffer.position();
        buffer.position(start);
        buffer.putShort((short) (flagMsb & 65535));
        buffer.position(end);
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        int flagMsb = buffer.getShort() & 65535;

        for (int i = 0; i < aextendedblockstorage.length; i++) {
            if ((flagMsb & 1 << i) != 0 && aextendedblockstorage[i] != null) {
                NibbleArray msb = aextendedblockstorage[i].getBlockMSBArray();
                if (msb == null) {
                    aextendedblockstorage[i].createBlockMSBArray();
                }
                buffer.get(aextendedblockstorage[i].getBlockMSBArray().data);
            } else if (sendUpdates && aextendedblockstorage[i] != null
                && aextendedblockstorage[i].getBlockMSBArray() != null) {
                    aextendedblockstorage[i].clearMSBArray();
                }
        }
    }
}
