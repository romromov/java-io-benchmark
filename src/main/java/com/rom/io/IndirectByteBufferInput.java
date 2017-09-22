package com.rom.io;

import java.nio.ByteBuffer;

/**
 * @author Roman Katerinenko
 */
final class IndirectByteBufferInput extends ByteBufferInput {
    IndirectByteBufferInput(TestParams testParams) {
        super(testParams, ByteBuffer::allocate);
    }

    @Override
    public String getName() {
        return "IndirectByteBufferInput";
    }
}