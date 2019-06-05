package com.dimach.fuelfix;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeTransformer implements IClassTransformer {

    public void removeByName(ClassNode cn, String name) {
        for (Iterator<MethodNode> iterator = cn.methods.iterator(); iterator.hasNext(); ) {
            if (iterator.next().name.equals(name)) {
                iterator.remove();
            }
        }
    }

    public byte[] preformTransform(byte[] bytes) {
        ClassNode cn = new ClassNode();
        ClassReader cr = new ClassReader(bytes);
        cr.accept(cn, 0);
        removeByName(cn, "testFuel");


        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        MethodVisitor mv;

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "testFuel", "(Ljava/lang/String;)Z", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(72, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn("fluid.rocketfuelmixa");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNE, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn("fuelgc");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label l2 = new Label();
            mv.visitJumpInsn(IFEQ, l2);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_1);
            Label l3 = new Label();
            mv.visitJumpInsn(GOTO, l3);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            mv.visitInsn(IRETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l4, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals("micdoodle8.mods.galacticraft.core.util.FluidUtil")) {
            return preformTransform(bytes);
        }
        return bytes;
    }

    public static boolean testFuel(String name) {
        return name.equals("rocket_fuel") || name.equals("fuelgc");
    }
}
