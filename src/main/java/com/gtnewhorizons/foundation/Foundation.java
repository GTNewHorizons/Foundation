package com.gtnewhorizons.foundation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizons.foundation.builtin.BiomeHandler;
import com.gtnewhorizons.foundation.builtin.BlockLSBHandler;
import com.gtnewhorizons.foundation.builtin.BlockLightHandler;
import com.gtnewhorizons.foundation.builtin.BlockMSBHandler;
import com.gtnewhorizons.foundation.builtin.MetadataHandler;
import com.gtnewhorizons.foundation.builtin.SkylightHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = Foundation.MODID,
    version = Tags.VERSION,
    name = "Foundation",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:gtnhlib@[0.6.22,);")
public class Foundation {

    public static final String MODID = "foundation";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BlockLSBHandler blockLSBHandler = new BlockLSBHandler();
        MetadataHandler metadataHandler = new MetadataHandler();
        BlockLightHandler blockLightHandler = new BlockLightHandler();
        SkylightHandler skylightHandler = new SkylightHandler();
        BlockMSBHandler blockMSBHandler = new BlockMSBHandler();
        BiomeHandler biomeHandler = new BiomeHandler();

        HandlerRegistry.registerChunkPacketHandler(blockLSBHandler);
        HandlerRegistry.registerChunkPacketHandler(metadataHandler);
        HandlerRegistry.registerChunkPacketHandler(blockLightHandler);
        HandlerRegistry.registerChunkPacketHandler(skylightHandler);
        HandlerRegistry.registerChunkPacketHandler(blockMSBHandler);
        HandlerRegistry.registerChunkPacketHandler(biomeHandler);

        HandlerRegistry.registerBlockPacketHandler(blockLSBHandler);
        HandlerRegistry.registerBlockPacketHandler(metadataHandler);
    }

}
