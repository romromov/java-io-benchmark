package com.rom.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static java.lang.String.format;

/**
 * @author Roman Katerinenko
 */
@SuppressWarnings("FieldCanBeLocal")
public final class TestHarness {
    private final Collection<Testable> testables = new ArrayList<>();
    private final long[] timesNano;
    private final TestParams testParams;

    private long minNanos;
    private long maxNanos;
    private long averageNanos;
    private long sygma;
    private Testable testable;
    private double sygmaPercent;

    public static void main(String... args) throws IOException {
        TestParams testParams = new TestParams()
                .setBufferSizeInBytes(1024)
                .setFilePath("1m.txt")
                .setFileSizeInBytes(1024 * 1024 * 1024)
                .setRepeats(2);
        System.out.printf("%s\n-------------------\n", testParams);
        TestHarness testHarness = new TestHarness(testParams);
//        testHarness.addTestable(new RandomByteBufferGenerator(FILE_NAME, FILE_SIZE_BYTES));
        testHarness.addTestable(new DirectByteBufferGenerator(testParams));
        testHarness.addTestable(new DirectByteBufferInput(testParams));
        testHarness.addTestable(new StreamsBasedGenerator(testParams));
        testHarness.addTestable(new StreamsBasedInput(testParams));
        testHarness.addTestable(new IndirectByteBufferGenerator(testParams));
        testHarness.addTestable(new IndirectByteBufferInput(testParams));
        testHarness.addTestable(new MemoryMappedGenerator(testParams));
        testHarness.addTestable(new MemoryMappedInput(testParams));
        testHarness.run();
    }

    TestHarness(TestParams testParams) {
        this.testParams = testParams;
        this.timesNano = new long[testParams.getRepeats()];
    }

    @SuppressWarnings("WeakerAccess")
    void addTestable(Testable testable) {
        testables.add(testable);
    }

    @SuppressWarnings("WeakerAccess")
    void run() throws IOException {
        for (Testable testable : testables) {
            this.testable = testable;
            for (int i = 0, n = testParams.getRepeats(); i < n; i++) {
                long now = System.nanoTime();
                testable.init();
                testable.run();
                testable.close();
                timesNano[i] = System.nanoTime() - now;
            }
            computeStats();
            printStats();
        }
    }

    private void computeStats() {
        minNanos = Arrays.stream(timesNano).min().orElseThrow(() -> new IllegalStateException("No min"));
        maxNanos = Arrays.stream(timesNano).max().orElseThrow(() -> new IllegalStateException("No max"));
        averageNanos = (long) Arrays.stream(timesNano).average().orElseThrow(() -> new IllegalStateException("No average"));
        sygma = computeSygma();
        long delta = maxNanos - minNanos;
        if (delta > 0) {
            sygmaPercent = ((double) sygma / (double) delta) * 100.;
        } else {
            sygmaPercent = 0;
        }
    }

    private void printStats() {
        System.out.printf("%s:\navg:%s\nmin:%s\nmax:%s\nsygma:%s(%f%%)\n",
                testable.getName(),
                formatNanos(averageNanos),
                formatNanos(minNanos),
                formatNanos(maxNanos),
                formatNanos(sygma),
                sygmaPercent);
        System.out.print("runs (millis):");
        Arrays.stream(timesNano).forEach(t -> System.out.format("%s, ", t / 1000000));
        System.out.println("\n");
        System.out.println("--------------------------");
    }

    private long computeSygma() {
        long sum = 0;
        for (long time : timesNano) {
            sum += (averageNanos - time) * (averageNanos - time);
        }
        return (long) (Math.sqrt(sum) / Math.sqrt(timesNano.length));
    }

    private String formatNanos(long nanos) {
        int mio = 1000000;
        if (nanos < mio) {
            return format("%f micros %s", nanos / 1000., getMbSfor(nanos));
        } else {
            return format("%f millis %s", nanos / (double) mio, getMbSfor(nanos));
        }
    }

    private String getMbSfor(long nanos) {
        double perSec = 1_000_000_000. / nanos;
        double megabytes = testParams.getFileSizeInBytes() / 1024. / 1024.;
        double MbS = megabytes * perSec; // megabytes/sec
        return format("%.3f (MB/s)", MbS);
    }
}