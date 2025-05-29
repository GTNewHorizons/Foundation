package com.gtnewhorizons.foundation.mixins.interfaces;

import com.gtnewhorizons.foundation.BlockPacketInfo;

public interface IMixinS23PacketBlockChange {

    void syncBlockPacketInfo(BlockPacketInfo info);

    BlockPacketInfo createBlockPacketInfo();
}
