package com.rom.io;

import java.nio.ByteBuffer;

/**
 * @author Roman Katerinenko
 */
@FunctionalInterface
interface BufferAllocator {
    ByteBuffer newByteBuffer(int sizeInBytes);
}