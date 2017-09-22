package com.rom.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Roman Katerinenko
 */
class StreamsBasedInput implements Testable {
    private final TestParams testParams;
    private BufferedInputStream stream;
    private byte[] buffer;

    StreamsBasedInput(TestParams testParams) {
        this.testParams = testParams;
    }

    @Override
    public void init() throws IOException {
        stream = new BufferedInputStream(new FileInputStream(testParams.getFilePath()));
        buffer = new byte[testParams.getBufferSizeInBytes()];
    }

    @Override
    public void run() throws IOException {
        while (stream.read(buffer) > 0) {
        }
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }

    @Override
    public String getName() {
        return "StreamsBasedInput";
    }
}