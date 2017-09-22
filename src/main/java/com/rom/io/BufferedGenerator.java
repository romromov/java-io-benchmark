package com.rom.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * @author Roman Katerinenko
 */
abstract class BufferedGenerator implements Testable {
    private final byte[] crBytes = format("%n").getBytes();
    private final TestParams testParams;

    private long remainderBytes;

    BufferedGenerator(TestParams testParams) {
        this.testParams = testParams;
    }

    protected byte[] getCrBytes() {
        return crBytes;
    }

    protected long getRemainderBytes() {
        return remainderBytes;
    }

    @Override
    public final void init() throws IOException {
        Path path = Paths.get(testParams.getFilePath());
        Files.deleteIfExists(path);
        Files.createFile(path);
        long bufferSize = testParams.getBufferSizeInBytes();
        long fileSize = testParams.getFileSizeInBytes();
        if (fileSize < bufferSize) {
            throw new IllegalStateException("File size cannot be less than " + bufferSize);
        }
        remainderBytes = fileSize;
        openFileAndBuffers();
    }

    protected abstract void openFileAndBuffers() throws IOException;


    @Override
    public final void run() throws IOException {
        while (true) {
            if (remainderBytes > 0) {
                remainderBytes -= generateNextLine();
            } else {
                break;
            }
        }
    }

    protected abstract long generateNextLine() throws IOException;

    protected TestParams getTestParams() {
        return testParams;
    }
}