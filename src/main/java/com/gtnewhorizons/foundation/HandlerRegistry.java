package com.gtnewhorizons.foundation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class HandlerRegistry {

    private static final List<ChunkPacketHandler> chunkPacketHandlers = new ArrayList<>();

    private static int chunkPacketBytes = 0;

    public static void registerChunkPacketHandler(ChunkPacketHandler handler) {
        chunkPacketBytes += handler.maxBytesPerChunk();
        chunkPacketHandlers.add(handler);
    }

    /**
     * Called by MixinS21PacketChunkData to handle creation of S21 packet data
     *
     * @param chunk         The chunk to be written to the packet
     * @param sendUpdates   Should chunk updates be sent to client
     * @param flagSubChunks Used as a mask for flagging subchunks(vertical slices)
     * @param data          The raw byte data for the packet
     * @return The final size of the byte buffer after all handlers have been processed
     */
    public static int writeChunkPackets(Chunk chunk, boolean sendUpdates, int flagSubChunks, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.putInt(chunkPacketHandlers.size()); // Count of handlers
        for (ChunkPacketHandler handler : chunkPacketHandlers) {
            /**
             * We need the length of each individual handler's data for when we read them.
             * We have to give a slice of the main buffer to each handler's read, so the only way to
             * know how big that slice needs to be is to populate the length of the data here.
             *
             * So we slice with enough room left in the buffer for an int, and then record the position
             * of this slice back into the main buffer at the position right before the slice so that
             * we have the real size of the data recorded for the read later.
             *
             * Also we can't use ByteBuffer.slice(start, length) because Java 8, so that's fun.
             */
            int sliceStart = buffer.position() + 4;
            int oldSize = buffer.limit();
            int oldPos = buffer.position();
            buffer.position(sliceStart);
            buffer.limit(sliceStart + handler.maxBytesPerChunk());
            ByteBuffer slice = buffer.slice();
            buffer.limit(oldSize);
            buffer.position(oldPos);
            handler.writeChunkPacket(chunk, sendUpdates, flagSubChunks, slice);
            int sliceLength = slice.position();
            buffer.putInt(sliceLength);
            buffer.position(sliceStart + sliceLength);
        }
        // S21PacketChunkData needs to know the full size of this buffer for inflate/deflate
        // So it will record it based on the return of this function in our mixin
        return buffer.position();
    }

    public static void readChunkPackets(Chunk chunk, boolean sendUpdates, int flagSubChunks, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int count = buffer.getInt();
        for (int i = 0; i < count; i++) {
            ChunkPacketHandler handler = chunkPacketHandlers.get(i);
            if (handler == null) {
                Foundation.LOG.error("Received unregistered chunk packet data");
                continue;
            }
            int sliceLength = buffer.getInt();
            int oldSize = buffer.limit();
            int oldPos = buffer.position();
            buffer.limit(buffer.position() + sliceLength);
            ByteBuffer slice = buffer.slice();
            buffer.limit(oldSize);
            buffer.position(oldPos);
            handler.readChunkPacket(chunk, sendUpdates, flagSubChunks, slice);
            buffer.position(buffer.position() + sliceLength);
        }
    }

    public static int getChunkPacketBytes() {
        return chunkPacketBytes;
    }
}
