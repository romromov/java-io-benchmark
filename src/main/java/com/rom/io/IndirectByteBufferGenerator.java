package com.rom.io;

import java.nio.ByteBuffer;

/**
 * @author Roman Katerinenko
 */
final class IndirectByteBufferGenerator extends ByteBufferGenerator {
    IndirectByteBufferGenerator(TestParams testParams) {
        super(testParams, ByteBuffer::allocate);
    }

    @Override
    public String getName() {
        return "IndirectByteBufferGenerator";
    }
}