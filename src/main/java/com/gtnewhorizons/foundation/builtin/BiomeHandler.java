package com.gtnewhorizons.foundation.builtin;

import java.nio.ByteBuffer;

import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class BiomeHandler implements ChunkPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return 256; // 16 * 16 bytes for biome data
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        if (sendUpdates) {
            byte[] biomeArray = chunk.getBiomeArray();
            buffer.put(biomeArray);
        }
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        if (sendUpdates) {
            byte[] biomeArray = chunk.getBiomeArray();
            buffer.get(biomeArray);
        }
    }
}
