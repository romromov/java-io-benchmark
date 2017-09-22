package com.rom.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Roman Katerinenko
 */
abstract class ByteBufferInput implements Testable {
    private final TestParams testParams;
    private final BufferAllocator bufferAllocator;
    private FileChannel channel;
    private ByteBuffer buffer;

    ByteBufferInput(TestParams testParams, BufferAllocator bufferAllocator) {
        this.testParams = testParams;
        this.bufferAllocator = bufferAllocator;
    }

    @Override
    public void init() throws IOException {
        channel = FileChannel.open(Paths.get(testParams.getFilePath()), StandardOpenOption.READ);
        buffer = bufferAllocator.newByteBuffer(testParams.getBufferSizeInBytes());
    }

    @Override
    public void run() throws IOException {
        while (channel.read(buffer) > 0) {
            buffer.flip();
        }
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }
}