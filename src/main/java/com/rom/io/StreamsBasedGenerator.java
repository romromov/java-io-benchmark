package com.rom.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Roman Katerinenko
 */
final class StreamsBasedGenerator extends BufferedGenerator {

    private BufferedOutputStream outputStream;

    StreamsBasedGenerator(TestParams testParams) {
        super(testParams);
    }

    @Override
    protected void openFileAndBuffers() throws FileNotFoundException {
        outputStream = new BufferedOutputStream(new FileOutputStream(getTestParams().getFilePath()));
    }

    @Override
    protected long generateNextLine() throws IOException {
        long nextLineLength = getCrBytes().length;
        long bytesToGenerate = Math.min(getRemainderBytes(), getTestParams().getBufferSizeInBytes() - nextLineLength);
        for (int i = 0; i < bytesToGenerate; i++) {
            outputStream.write(VALUE);
        }
        outputStream.write(getCrBytes());
        return bytesToGenerate + nextLineLength;
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Override
    public String getName() {
        return "StreamsBasedGenerator";
    }
}