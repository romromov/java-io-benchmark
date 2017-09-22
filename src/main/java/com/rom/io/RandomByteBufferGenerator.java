package com.rom.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

import static java.lang.String.format;

/**
 * @author Roman Katerinenko
 */
final class RandomByteBufferGenerator {
    private static final String NAME = "RandomByteBufferGenerator";
    private static final int MAX_LINE_SIZE_BYTES = 1024;
    private static final int SEED = 123;
    private static final int MAX_DEVIATION = 10;
    private static final byte Z = 'z';
    private static final byte ZERO = '0';

    private final byte[] nextLineCharBytes = format("%n").getBytes();
    private final ByteBuffer lineBuffer;
    private final Random random;
    private final String filePath;
    private final long fileSizeInBytes;


    private long remainderBytes;

    protected RandomByteBufferGenerator(String pathStr, long sizeInBytes) {
        filePath = pathStr;
        fileSizeInBytes = sizeInBytes;
        lineBuffer = ByteBuffer.allocate(MAX_LINE_SIZE_BYTES);
        random = new Random(SEED);
    }

    protected String getName() {
        return NAME;
    }

    protected void init() {
        if (fileSizeInBytes < MAX_LINE_SIZE_BYTES) {
            throw new IllegalStateException("File size cannot be less than " + MAX_LINE_SIZE_BYTES);
        }
        remainderBytes = fileSizeInBytes;
    }

    protected void generate() throws IOException {
        generateFile();
    }

    protected void close() {
    }

    private void generateFile() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             FileChannel channel = fos.getChannel()) {
            while (true) {
                generateNextRandomLine();
                if (lineBuffer.position() == 0) {
                    break;
                } else {
                    lineBuffer.flip();
                    channel.write(lineBuffer);
                }
            }
        }
    }

    private void generateNextRandomLine() {
        lineBuffer.clear();
        if (remainderBytes > 0) {
            long randomLength = MAX_LINE_SIZE_BYTES - random.nextInt(MAX_DEVIATION);
            long lineLengthBytes = Math.min(randomLength, remainderBytes);
            generateNextRandomLine(lineLengthBytes);
            remainderBytes -= lineLengthBytes;
        }
    }

    private void generateNextRandomLine(long length) {
        for (int i = 0; i < length - nextLineCharBytes.length; i++) {
            lineBuffer.put(getNextRandomSymbol());
        }
        lineBuffer.put(nextLineCharBytes);
    }

    private byte getNextRandomSymbol() {
        return (byte) (random.nextInt(Z - ZERO) + ZERO);
    }
}