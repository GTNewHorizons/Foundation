package com.gtnewhorizons.foundation;

import net.minecraft.block.Block;

public class BlockPacketInfo {

    private int x;
    private int y;
    private int z;
    private Block block;
    private int metadata;

    public BlockPacketInfo(int x, int y, int z) {
        this(x, y, z, null, 0);
    }

    public BlockPacketInfo(int x, int y, int z, Block block, int metadata) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.metadata = metadata;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getMetadata() {
        return metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

}
