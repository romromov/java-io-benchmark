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
final class MemoryMappedGenerator extends BufferedGenerator {
    private ByteBuffer fileBuffer;
    private FileChannel fileChannel;

    MemoryMappedGenerator(TestParams testParams) {
        super(testParams);
    }

    @Override
    public void close() throws IOException {
        if (fileChannel!=null){
            fileChannel.close();
        }
    }

    @Override
    public String getName() {
        return "MemoryMappedGenerator";
    }

    @Override
    protected void openFileAndBuffers() throws IOException {
        fileChannel = FileChannel.open(Paths.get(getTestParams().getFilePath()), WRITE, READ);
        fileBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, getTestParams().getFileSizeInBytes());
    }

    @Override
    protected long generateNextLine() throws IOException {
        long length = getCrBytes().length;
        long bytesToGenerate = Math.min(getRemainderBytes(), getTestParams().getBufferSizeInBytes() - length);
        for (int i = 0; i < bytesToGenerate; i++) {
            fileBuffer.put(VALUE);
        }
        fileBuffer.put(getCrBytes());
        return bytesToGenerate + length;
    }
}