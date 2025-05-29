package com.gtnewhorizons.foundation.mixins.early;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.HandlerRegistry;
import com.gtnewhorizons.foundation.mixins.interfaces.IMixinS23PacketBlockChange;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S23PacketBlockChange.class)
public abstract class MixinS23PacketBlockChange implements IMixinS23PacketBlockChange {

    @Shadow
    private int field_148887_a; // x

    @Shadow
    private int field_148885_b; // y

    @Shadow
    private int field_148886_c; // z

    @Shadow
    public Block field_148883_d; // block

    @Shadow
    public int field_148884_e; // metadata

    @Overwrite
    public void writePacketData(PacketBuffer data) {
        data.writeInt(this.field_148887_a);
        data.writeByte(this.field_148885_b);
        data.writeInt(this.field_148886_c);
        HandlerRegistry.writeS23Packets((S23PacketBlockChange) (Object) this, data);
    }

    @Overwrite
    public void readPacketData(PacketBuffer data) {
        this.field_148887_a = data.readInt();
        this.field_148885_b = data.readUnsignedByte();
        this.field_148886_c = data.readInt();
        HandlerRegistry.readS23Packets((S23PacketBlockChange) (Object) this, data);
    }

    @Override
    public void syncBlockPacketInfo(BlockPacketInfo info) {
        field_148887_a = info.getX();
        field_148885_b = info.getY();
        field_148886_c = info.getZ();
        field_148883_d = info.getBlock();
        field_148884_e = info.getMetadata();
    }

    @Override
    public BlockPacketInfo createBlockPacketInfo() {
        return new BlockPacketInfo(field_148887_a, field_148885_b, field_148886_c, field_148883_d, field_148884_e);
    }
}
