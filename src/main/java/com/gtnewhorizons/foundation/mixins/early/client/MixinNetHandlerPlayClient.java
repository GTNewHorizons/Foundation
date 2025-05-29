package com.gtnewhorizons.foundation.mixins.early.client;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.mixins.interfaces.IMixinS22PacketMultiBlockChange;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Shadow
    private WorldClient clientWorldController;

    @Overwrite
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
        IMixinS22PacketMultiBlockChange mixinPacket = (IMixinS22PacketMultiBlockChange) packetIn;
        int chunkX = packetIn.func_148920_c().chunkXPos * 16;
        int chunkZ = packetIn.func_148920_c().chunkZPos * 16;

        for (BlockPacketInfo info : mixinPacket.getInfos()) {
            clientWorldController.func_147492_c(
                info.getX() + chunkX,
                info.getY(),
                info.getZ() + chunkZ,
                info.getBlock(),
                info.getMetadata());
            // TODO: Do we need to provide a hook here for mods to do something on the packet handling?
            // If we do need to do that, we probably need to add one for the single block change as well.
        }
    }

}
