/**********************************************************************
Copyright (c) 2007 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.enhancer.methods;

import org.datanucleus.asm.Label;
import org.datanucleus.asm.Opcodes;
import org.datanucleus.enhancer.ClassEnhancer;
import org.datanucleus.enhancer.ClassMethod;
import org.datanucleus.enhancer.EnhanceUtils;

/**
 * Method to generate the method "dnGetVersion" using ASM.
 * <pre>
 * public final Object dnGetVersion()
 * {
 *     if (dnStateManager != null)
 *         return dnStateManager.getVersion(this);
 *     if (this.dnIsDetached() != true)
 *         return null;
 *     return dnDetachedState[1];
 * }
 * </pre>
 * or (when not Detachable)
 * <pre>
 * public final Object dnGetVersion()
 * {
 *     if (dnStateManager != null)
 *         return dnStateManager.getVersion(this);
 *     return null;
 * }
 * </pre>
 */
public class GetVersion extends ClassMethod
{
    public static GetVersion getInstance(ClassEnhancer enhancer)
    {
        return new GetVersion(enhancer, enhancer.getNamer().getGetVersionMethodName(), Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
            Object.class, null, null);
    }

    /**
     * Constructor.
     * @param enhancer ClassEnhancer
     * @param name Name of method
     * @param access Access type
     * @param returnType Return type
     * @param argTypes Argument types
     * @param argNames Argument names
     */
    public GetVersion(ClassEnhancer enhancer, String name, int access, 
        Object returnType, Object[] argTypes, String[] argNames)
    {
        super(enhancer, name, access, returnType, argTypes, argNames);
    }

    /**
     * Method to add the contents of the class method.
     */
    public void execute()
    {
        visitor.visitCode();

        Label l0 = new Label();
        visitor.visitLabel(l0);
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitFieldInsn(Opcodes.GETFIELD, getClassEnhancer().getASMClassName(),
            getNamer().getStateManagerFieldName(), getNamer().getStateManagerDescriptor());
        Label l1 = new Label();
        visitor.visitJumpInsn(Opcodes.IFNULL, l1);
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitFieldInsn(Opcodes.GETFIELD, getClassEnhancer().getASMClassName(),
            getNamer().getStateManagerFieldName(), getNamer().getStateManagerDescriptor());
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, getNamer().getStateManagerAsmClassName(),
            "getVersion", "(" + getNamer().getPersistableDescriptor() + ")" + EnhanceUtils.CD_Object);
        visitor.visitInsn(Opcodes.ARETURN);
        visitor.visitLabel(l1);
        visitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

        if (!enhancer.getClassMetaData().isDetachable())
        {
            visitor.visitInsn(Opcodes.ACONST_NULL);
            visitor.visitInsn(Opcodes.ARETURN);

            Label l3 = new Label();
            visitor.visitLabel(l3);
            visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, l0, l3, 0);
            visitor.visitMaxs(2, 1);
        }
        else
        {
            visitor.visitVarInsn(Opcodes.ALOAD, 0);
            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, getClassEnhancer().getASMClassName(),
                getNamer().getIsDetachedMethodName(), "()Z");
            Label l3 = new Label();
            visitor.visitJumpInsn(Opcodes.IFNE, l3);
            visitor.visitInsn(Opcodes.ACONST_NULL);
            visitor.visitInsn(Opcodes.ARETURN);
            visitor.visitLabel(l3);
            visitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

            visitor.visitVarInsn(Opcodes.ALOAD, 0);
            visitor.visitFieldInsn(Opcodes.GETFIELD, getClassEnhancer().getASMClassName(),
                getNamer().getDetachedStateFieldName(), "[" + EnhanceUtils.CD_Object);
            visitor.visitInsn(Opcodes.ICONST_1);
            visitor.visitInsn(Opcodes.AALOAD);
            visitor.visitInsn(Opcodes.ARETURN);

            Label l4 = new Label();
            visitor.visitLabel(l4);
            visitor.visitLocalVariable("this", getClassEnhancer().getClassDescriptor(), null, l0, l4, 0);
            visitor.visitMaxs(2, 1);
        }

        visitor.visitEnd();
    }
}
