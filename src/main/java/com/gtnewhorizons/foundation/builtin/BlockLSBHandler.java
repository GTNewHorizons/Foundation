package com.gtnewhorizons.foundation.builtin;

import java.nio.ByteBuffer;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.api.BlockPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;

public class BlockLSBHandler implements ChunkPacketHandler, BlockPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return 4096 * 16; // 16 * 16 * 16 bytes per section * 16 sections
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        int start = buffer.position();

        for (int i = 0; i < aextendedblockstorage.length; i++) {
            if (aextendedblockstorage[i] != null && (!sendUpdates || !aextendedblockstorage[i].isEmpty())
                && (flagSubChunks & 1 << i) != 0) {
                byte[] lsb = aextendedblockstorage[i].getBlockLSBArray();
                buffer.put(lsb);
            }
        }
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer buffer) {
        ExtendedBlockStorage[] aextendedblockstorage = chunk.getBlockStorageArray();
        for (int i = 0; i < aextendedblockstorage.length; i++) {
            if ((flagSubChunks & 1 << i) != 0 && aextendedblockstorage[i] != null) {
                buffer.get(aextendedblockstorage[i].getBlockLSBArray());
            }
        }
    }

    @Override
    public void writeBlockPacket(BlockPacketInfo info, PacketBuffer data) {
        data.writeVarIntToBuffer(Block.getIdFromBlock(info.getBlock()));
    }

    @Override
    public void readBlockPacket(BlockPacketInfo info, PacketBuffer data) {
        info.setBlock(Block.getBlockById(data.readVarIntFromBuffer()));
    }
}
