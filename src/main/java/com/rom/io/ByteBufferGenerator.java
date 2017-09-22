package com.rom.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * @author Roman Katerinenko
 */
abstract class ByteBufferGenerator extends BufferedGenerator {
    private final BufferAllocator bufferAllocator;

    private ByteBuffer lineBuffer;
    private FileChannel fileChannel;

    ByteBufferGenerator(TestParams testParams, BufferAllocator buffAllocator) {
        super(testParams);
        this.bufferAllocator = buffAllocator;
    }

    @Override
    protected void openFileAndBuffers() throws IOException {
        fileChannel = FileChannel.open(Paths.get(getTestParams().getFilePath()), WRITE, READ);
        lineBuffer = bufferAllocator.newByteBuffer(getTestParams().getBufferSizeInBytes());
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }

    @Override
    protected long generateNextLine() throws IOException {
        lineBuffer.clear();
        long length = getCrBytes().length;
        long bytesToGenerate = Math.min(getRemainderBytes(), getTestParams().getBufferSizeInBytes() - length);
        for (int i = 0; i < bytesToGenerate; i++) {
            lineBuffer.put(VALUE);
        }
        lineBuffer.put(getCrBytes());
        lineBuffer.flip();
        fileChannel.write(lineBuffer);
        return bytesToGenerate + length;
    }

}
