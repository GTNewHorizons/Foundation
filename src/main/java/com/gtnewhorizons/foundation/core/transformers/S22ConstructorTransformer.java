package com.gtnewhorizons.foundation.core.transformers;

import com.gtnewhorizons.foundation.core.IFoundationTransformer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.*;

public class S22ConstructorTransformer implements IFoundationTransformer {

    @Override
    public String[] getTargetClasses() {
        return new String[] { "net.minecraft.network.play.server.S22PacketMultiBlockChange" };
    }

    @Override
    public void transform(ClassNode cn) {
        // We are basically trying to @Overwrite the constructor for this class. We're nuking the entire instruction set
        // from the MethodNode, reinserting the super call and return to make it valid, and adding a call to a newConstructor
        // function which is added by MixinS22PacketMultiBlockChange and the interface it applies. The various loads to that
        // invocation are basically just passing through the constructor arguments to the new method.
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("<init>") && mn.desc.equals("(I[SLnet/minecraft/world/chunk/Chunk;)V")) {
                InsnList instructions = new InsnList();

                // Call super()
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/network/Packet", "<init>", "()V"));

                // Load parameters and call new constructor interface method
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "com/gtnewhorizons/foundation/mixins/interfaces/IMixinS22PacketMultiBlockChange", "newConstructor", "(I[SLnet/minecraft/world/chunk/Chunk;)V"));

                // We need a return
                instructions.add(new InsnNode(Opcodes.RETURN));

                // Clear the method instructions and insert our new ones
                mn.instructions.clear();
                mn.tryCatchBlocks.clear();
                mn.instructions.insert(instructions);
            }
        }
    }
}
