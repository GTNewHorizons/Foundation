package com.gtnewhorizons.foundation.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.transformers.MixinClassWriter;

import java.util.List;

public class FoundationTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (basicClass == null) {
            return null;
        }

        final List<FoundationTransformers> transformers = FoundationTransformers.get(transformedName);
        if (transformers == null) {
            return basicClass;
        }

        final ClassNode cn = new ClassNode();
        final ClassReader cr = new ClassReader(basicClass);
        cr.accept(cn, 0);

        for (FoundationTransformers transformer : transformers) {
            transformer.transform(cn);
        }

        final MixinClassWriter cw = new MixinClassWriter(MixinClassWriter.COMPUTE_MAXS | MixinClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
