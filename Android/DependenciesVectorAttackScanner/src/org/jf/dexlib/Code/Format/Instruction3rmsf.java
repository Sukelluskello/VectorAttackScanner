/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.Code.Format;

import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.Code.InstructionWithReference;
import org.jf.dexlib.Code.RegisterRangeInstruction;
import org.jf.dexlib.MethodIdItem;
import org.jf.dexlib.Util.AnnotatedOutput;

public class Instruction3rmsf extends InstructionWithReference implements RegisterRangeInstruction {
    private final Instruction3rms unfixedInstruction;

    public Instruction3rmsf(Opcode opcode, Instruction3rms unfixedInstruction, MethodIdItem method) {
        //the opcode should be the "fixed" opcode. i.e. iget-object, etc. (NOT the "quick" version)
        super(opcode, method);
        this.unfixedInstruction = unfixedInstruction;
    }

    protected void writeInstruction(AnnotatedOutput out, int currentCodeAddress) {
        short regCount = getRegCount();
        int startReg = getStartRegister();

        out.writeByte(opcode.value);
        out.writeByte(regCount);
        out.writeShort(this.getReferencedItem().getIndex());
        out.writeShort(startReg);
    }

    public Format getFormat() {
        return Format.Format3rmsf;
    }

    public int getStartRegister() {
        return unfixedInstruction.getStartRegister();
    }

    public short getRegCount() {
        return unfixedInstruction.getRegCount();
    }
}
