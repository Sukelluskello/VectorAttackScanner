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

package org.jf.dexlib.EncodedValue;

import org.jf.dexlib.Util.Input;
import org.jf.dexlib.Util.AnnotatedOutput;
import org.jf.dexlib.DexFile;

public class ArrayEncodedValue extends ArrayEncodedSubValue {
    /**
     * Constructs a new <code>ArrayEncodedValue</code> by reading the value from the given <code>Input</code> object.
     * The <code>Input</code>'s cursor should be set to the 2nd byte of the encoded value
     * @param dexFile The <code>DexFile</code> that is being read in
     * @param in The <code>Input</code> object to read from
     */
    protected ArrayEncodedValue(DexFile dexFile, Input in) {
        super(dexFile, in);
    }

    /**
     * Constructs a new <code>ArrayEncodedValue</code> with the given values
     * @param values The array values
     */
    public ArrayEncodedValue(EncodedValue[] values) {
        super(values);
    }

    /** {@inheritDoc} */
    public void writeValue(AnnotatedOutput out) {
        if (out.annotates()) {
            out.annotate("value_type=" + ValueType.VALUE_ARRAY.name() + ",value_arg=0");
        }
        out.writeByte(ValueType.VALUE_ARRAY.value);
        super.writeValue(out);
    }

    /** {@inheritDoc} */
    public int placeValue(int offset) {
        return super.placeValue(offset + 1);
    }
}
