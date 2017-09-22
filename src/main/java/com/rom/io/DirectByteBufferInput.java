package com.rom.io;

import java.nio.ByteBuffer;

/**
 * @author Roman Katerinenko
 */
final class DirectByteBufferInput extends ByteBufferInput {
    DirectByteBufferInput(TestParams testParams) {
        super(testParams, ByteBuffer::allocateDirect);
    }

    @Override
    public String getName() {
        return "DirectByteBufferInput";
    }
}