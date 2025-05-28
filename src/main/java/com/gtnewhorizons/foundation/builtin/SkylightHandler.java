package com.gtnewhorizons.foundation.builtin;

import java.nio.ByteBuffer;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class SkylightHandler implements ChunkPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return 2048 * 16; // 16 * 16 * 16 / 2 bytes per chunk section
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        if (!chunk.worldObj.provider.hasNoSky) {
            ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
            for (int l = 0; l < aextendedblockstorage.length; ++l) {
                if (aextendedblockstorage[l] != null && (!sendUpdates || !aextendedblockstorage[l].isEmpty())
                    && (flagSubChunks & 1 << l) != 0) {
                    NibbleArray nibblearray = aextendedblockstorage[l].getSkylightArray();
                    buffer.put(nibblearray.data);
                }
            }
        }
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        for (int l = 0; l < aextendedblockstorage.length; ++l) {
            if (aextendedblockstorage[l] != null && (flagSubChunks & 1 << l) != 0) {
                NibbleArray nibblearray = aextendedblockstorage[l].getSkylightArray();
                buffer.get(nibblearray.data);
            }
        }
    }
}
