package com.rom.io;

import java.nio.ByteBuffer;

/**
 * @author Roman Katerinenko
 */
final class DirectByteBufferGenerator extends ByteBufferGenerator {
    DirectByteBufferGenerator(TestParams testParams) {
        super(testParams, ByteBuffer::allocateDirect);
    }

    @Override
    public String getName() {
        return "DirectByteBufferGenerator";
    }
}