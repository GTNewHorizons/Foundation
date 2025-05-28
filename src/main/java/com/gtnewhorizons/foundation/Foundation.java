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
        HandlerRegistry.registerChunkPacketHandler(new BlockLSBHandler());
        HandlerRegistry.registerChunkPacketHandler(new MetadataHandler());
        HandlerRegistry.registerChunkPacketHandler(new BlockLightHandler());
        HandlerRegistry.registerChunkPacketHandler(new SkylightHandler());
        HandlerRegistry.registerChunkPacketHandler(new BlockMSBHandler());
        HandlerRegistry.registerChunkPacketHandler(new BiomeHandler());
    }

}
