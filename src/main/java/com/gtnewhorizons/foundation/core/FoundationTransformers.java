package com.gtnewhorizons.foundation.core;

import com.gtnewhorizons.foundation.core.transformers.S22ConstructorTransformer;
import org.spongepowered.asm.lib.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FoundationTransformers {

    S22ConstructorTransformer(new S22ConstructorTransformer());

    private static final Map<String, List<FoundationTransformers>> transformers = new HashMap<>();
    private final IFoundationTransformer transformer;

    static {
        for (final FoundationTransformers entry : values()) {
            for (final String className : entry.transformer.getTargetClasses()) {
                FoundationTransformers.transformers.putIfAbsent(className, new ArrayList<>());
                FoundationTransformers.transformers.get(className).add(entry);
            }
        }
    }

    public static List<FoundationTransformers> get(final String className) {
        return FoundationTransformers.transformers.get(className);
    }

    FoundationTransformers(final IFoundationTransformer transformer) {
        this.transformer = transformer;
    }

    public void transform(final ClassNode cn) { transformer.transform(cn); }

}
