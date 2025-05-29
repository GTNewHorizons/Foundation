package com.gtnewhorizons.foundation.core;

import org.spongepowered.asm.lib.tree.ClassNode;

public interface IFoundationTransformer {

    String[] getTargetClasses();

    void transform(final ClassNode cn);
}
