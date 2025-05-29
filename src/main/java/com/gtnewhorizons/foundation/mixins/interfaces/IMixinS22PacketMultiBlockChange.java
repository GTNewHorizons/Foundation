package com.gtnewhorizons.foundation.mixins.interfaces;

import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizons.foundation.BlockPacketInfo;

public interface IMixinS22PacketMultiBlockChange {

    void newConstructor(int count, short[] positions, Chunk chunk);

    BlockPacketInfo[] getInfos();

}
