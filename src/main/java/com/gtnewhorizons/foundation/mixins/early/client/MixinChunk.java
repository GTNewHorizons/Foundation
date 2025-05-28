package com.gtnewhorizons.foundation.mixins.early.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.foundation.HandlerRegistry;

@Mixin(Chunk.class)
public class MixinChunk {

    @Shadow
    public Map<ChunkPosition, TileEntity> chunkTileEntityMap;

    @Shadow
    public World worldObj;

    @Shadow
    private ExtendedBlockStorage[] storageArrays;

    @Shadow
    public boolean isLightPopulated;

    @Shadow
    public boolean isTerrainPopulated;

    @Overwrite
    public void fillChunk(byte[] data, int flagSubChunks, int flagMsb, boolean sendUpdates) {
        Chunk thisObject = (Chunk) (Object) this;
        Iterator iterator = chunkTileEntityMap.values()
            .iterator();
        while (iterator.hasNext()) {
            TileEntity tileEntity = (TileEntity) iterator.next();
            tileEntity.updateContainingBlockInfo();
            tileEntity.getBlockMetadata();
            tileEntity.getBlockType();
        }

        int k = 0;
        boolean flag1 = !this.worldObj.provider.hasNoSky;
        int l;

        for (l = 0; l < this.storageArrays.length; ++l) {
            if ((flagSubChunks & 1 << l) != 0) {
                if (this.storageArrays[l] == null) {
                    this.storageArrays[l] = new ExtendedBlockStorage(l << 4, flag1);
                }
            } else if (sendUpdates && this.storageArrays[l] != null) {
                this.storageArrays[l] = null;
            }
        }

        HandlerRegistry.readChunkPackets(thisObject, sendUpdates, flagSubChunks, data);

        for (l = 0; l < this.storageArrays.length; ++l) {
            if (this.storageArrays[l] != null && (flagSubChunks & 1 << l) != 0) {
                this.storageArrays[l].removeInvalidBlocks();
            }
        }

        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        thisObject.generateHeightMap();
        List<TileEntity> invalidList = new ArrayList<TileEntity>();
        iterator = this.chunkTileEntityMap.values()
            .iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();
            int x = tileentity.xCoord & 15;
            int y = tileentity.yCoord;
            int z = tileentity.zCoord & 15;
            Block block = tileentity.getBlockType();
            if ((block != thisObject.getBlock(x, y, z)
                || tileentity.blockMetadata != thisObject.getBlockMetadata(x, y, z))
                && tileentity.shouldRefresh(
                    block,
                    thisObject.getBlock(x, y, z),
                    tileentity.blockMetadata,
                    thisObject.getBlockMetadata(x, y, z),
                    worldObj,
                    x,
                    y,
                    z)) {
                invalidList.add(tileentity);
            }
            tileentity.updateContainingBlockInfo();
        }

        for (TileEntity te : invalidList) {
            te.invalidate();
        }
    }

}
