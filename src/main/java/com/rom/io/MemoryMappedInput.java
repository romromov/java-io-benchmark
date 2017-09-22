package com.rom.io;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Roman Katerinenko
 */
final class MemoryMappedInput implements Testable {
    private final TestParams testParams;

    private FileChannel channel;
    private MappedByteBuffer buffer;
    private byte[] array;

    MemoryMappedInput(TestParams testParams) {
        this.testParams = testParams;
    }

    @Override
    public void init() throws IOException {
        channel = FileChannel.open(Paths.get(testParams.getFilePath()), StandardOpenOption.READ);
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        array = new byte[testParams.getBufferSizeInBytes()];
    }

    @Override
    public void run() throws IOException {
        while (buffer.get(array).position() < buffer.limit()) {
        }
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public String getName() {
        return "MemoryMappedInput";
    }
}