package com.gtnewhorizons.foundation.builtin;

import java.nio.ByteBuffer;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.api.BlockPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class MetadataHandler implements ChunkPacketHandler, BlockPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return 2048 * 16; // 16 * 16 * 16 / 2 bytes per chunk section
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        for (int l = 0; l < aextendedblockstorage.length; ++l) {
            if (aextendedblockstorage[l] != null && (!sendUpdates || !aextendedblockstorage[l].isEmpty())
                && (flagSubChunks & 1 << l) != 0) {
                NibbleArray nibblearray = aextendedblockstorage[l].getMetadataArray();
                buffer.put(nibblearray.data);
            }
        }
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        for (int l = 0; l < aextendedblockstorage.length; ++l) {
            if (aextendedblockstorage[l] != null && (flagSubChunks & 1 << l) != 0) {
                NibbleArray nibblearray = aextendedblockstorage[l].getMetadataArray();
                buffer.get(nibblearray.data);
            }
        }
    }

    @Override
    public void writeBlockPacket(BlockPacketInfo info, PacketBuffer data) {
        data.writeByte(info.getMetadata());
    }

    @Override
    public void readBlockPacket(BlockPacketInfo info, PacketBuffer data) {
        info.setMetadata(data.readUnsignedByte());
    }
}
