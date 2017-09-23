package com.rom.io;

import java.io.IOException;

/**
 * @author Roman Katerinenko
 */
public class Shell {
    public static void main(String... args) throws IOException {
        TestParams testParams = parseArgs(args);
        if (testParams != null) {
            TestHarness.runAllTestsWith(testParams);
        }
    }

    private static TestParams parseArgs(String... args) {
        if (args.length < 4) {
            System.out.println("Must be 4 arguments: bufferSizeBytes, fileSizeBytes, filePath, repeats");
            return null;
        }
        return new TestParams()
                .setBufferSizeInBytes(Integer.valueOf(args[0]))
                .setFileSizeInBytes(Long.valueOf(args[1]))
                .setFilePath(args[2])
                .setRepeats(Integer.valueOf(args[3]));
    }
}