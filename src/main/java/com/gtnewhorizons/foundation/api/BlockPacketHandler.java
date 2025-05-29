package com.gtnewhorizons.foundation.api;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.foundation.BlockPacketInfo;

public interface BlockPacketHandler {

    void writeBlockPacket(BlockPacketInfo info, PacketBuffer data);

    void readBlockPacket(BlockPacketInfo info, PacketBuffer data);

}
