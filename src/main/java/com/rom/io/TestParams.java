package com.rom.io;

/**
 * @author Roman Katerinenko
 */
final class TestParams {
    private String filePath;
    private long fileSizeInBytes;
    private int bufferSizeInBytes;
    private int repeats;

    int getRepeats() {
        return repeats;
    }

    TestParams setRepeats(int repeats) {
        this.repeats = repeats;
        return this;
    }

    String getFilePath() {
        return filePath;
    }

    TestParams setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    long getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    TestParams setFileSizeInBytes(long fileSizeInBytes) {
        this.fileSizeInBytes = fileSizeInBytes;
        return this;
    }

    int getBufferSizeInBytes() {
        return bufferSizeInBytes;
    }

    TestParams setBufferSizeInBytes(int bufferSizeInBytes) {
        this.bufferSizeInBytes = bufferSizeInBytes;
        return this;
    }

    @Override
    public String toString() {
        return "TestParams{" +
                "filePath='" + filePath + '\'' +
                ", fileSizeInBytes=" + fileSizeInBytes +
                ", bufferSizeInBytes=" + bufferSizeInBytes +
                ", repeats=" + repeats +
                '}';
    }
}