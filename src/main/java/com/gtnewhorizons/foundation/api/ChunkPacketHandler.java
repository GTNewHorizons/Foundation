package com.gtnewhorizons.foundation.api;

import java.nio.ByteBuffer;

import net.minecraft.world.chunk.Chunk;

public interface ChunkPacketHandler {

    int maxBytesPerChunk();

    void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer);

    void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer);
}
