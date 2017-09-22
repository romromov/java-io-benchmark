package com.rom.io;

import java.io.IOException;

/**
 * @author Roman Katerinenko
 */

interface Testable {
    byte VALUE = (byte) 123;

    void init() throws IOException;

    void run() throws IOException;

    void close() throws IOException;

    String getName();
}