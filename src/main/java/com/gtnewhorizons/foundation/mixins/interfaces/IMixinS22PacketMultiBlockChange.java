package com.gtnewhorizons.foundation.mixins.interfaces;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import net.minecraft.world.chunk.Chunk;

public interface IMixinS22PacketMultiBlockChange {

    void newConstructor(int count, short[] positions, Chunk chunk);

    BlockPacketInfo[] getInfos();

}
