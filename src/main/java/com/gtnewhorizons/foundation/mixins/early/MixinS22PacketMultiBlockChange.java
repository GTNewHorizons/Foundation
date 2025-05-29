package com.gtnewhorizons.foundation.mixins.early;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.Foundation;
import com.gtnewhorizons.foundation.HandlerRegistry;
import com.gtnewhorizons.foundation.mixins.interfaces.IMixinS22PacketMultiBlockChange;

@Mixin(S22PacketMultiBlockChange.class)
public class MixinS22PacketMultiBlockChange implements IMixinS22PacketMultiBlockChange {

    @Shadow
    private ChunkCoordIntPair field_148925_b;

    @Unique
    private BlockPacketInfo[] blockPackets;

    public void newConstructor(int count, short[] positions, Chunk chunk) {
        field_148925_b = new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition);
        blockPackets = new BlockPacketInfo[count];
        for (int i = 0; i < count; i++) {
            int x = positions[i] >> 12 & 15;
            int z = positions[i] >> 8 & 15;
            int y = positions[i] & 255;
            Block block = chunk.getBlock(x, y, z);
            int metadata = chunk.getBlockMetadata(x, y, z);
            blockPackets[i] = new BlockPacketInfo(x, y, z, block, metadata);
        }

        Foundation.LOG.info("Hello from S22 ASM");
    }

    @Overwrite
    public void readPacketData(PacketBuffer data) {
        field_148925_b = new ChunkCoordIntPair(data.readInt(), data.readInt());
        int count = data.readInt();
        blockPackets = new BlockPacketInfo[count];
        for (int i = 0; i < count; i++) {
            int position = data.readShort() & 65535;
            int x = position >> 12 & 15;
            int z = position >> 8 & 15;
            int y = position & 255;
            blockPackets[i] = new BlockPacketInfo(x, y, z);
            HandlerRegistry.readS22Packets(blockPackets[i], data);
        }
    }

    @Overwrite
    public void writePacketData(PacketBuffer data) {
        data.writeInt(field_148925_b.chunkXPos);
        data.writeInt(field_148925_b.chunkZPos);

        if (blockPackets == null) {
            data.writeInt(0);
        } else {
            data.writeInt(blockPackets.length);
            for (BlockPacketInfo info : blockPackets) {
                int packedPosition = ((info.getX() & 15) << 12) | ((info.getZ() & 15) << 8) | (info.getY() & 255);
                data.writeShort(packedPosition);
                HandlerRegistry.writeS22Packets(info, data);
            }
        }
    }

    @Override
    public BlockPacketInfo[] getInfos() {
        return blockPackets;
    }

}
