package com.gtnewhorizons.foundation.api;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import net.minecraft.network.PacketBuffer;

public interface BlockPacketHandler {

    void writeBlockPacket(BlockPacketInfo info, PacketBuffer data);

    void readBlockPacket(BlockPacketInfo info, PacketBuffer data);

}
