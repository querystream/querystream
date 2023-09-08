
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.test.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * A {@link PrintStream} that can capture what gets printed.
 */
public class CapturePrintStream extends PrintStream {

    private ByteArrayOutputStream buf;

// Constructor

    public CapturePrintStream(PrintStream out, boolean autoFlush, String charset) throws UnsupportedEncodingException {
        super(out, autoFlush, charset);
    }

// Factory Methods

    /**
     * Create an instance.
     *
     * @param out underlying output stream
     * @return new stream
     * @throws IllegalArgumentException if {@code out} is null
     */
    public static CapturePrintStream of(PrintStream out) {
        if (out == null)
            throw new IllegalArgumentException("null out");
        try {
            return new CapturePrintStream(out, false, "UTF-8"); // using JDK 8 compatible constructor; charset doesn't matter
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unexpected error", e);
        }
    }

// Public Methods

    /**
     * Start capture.
     *
     * @return true if capture was previously stopped, false if already capturing
     */
    public synchronized boolean startCapture() {
        if (this.buf != null)
            return false;
        this.buf = new ByteArrayOutputStream();
        return true;
    }

    /**
     * Stop capture and return the captured data.
     *
     * @return captured output if capture was previously started, or null if not capturing
     */
    public synchronized byte[] stopCapture() {
        if (this.buf == null)
            return null;
        final byte[] capture = this.buf.toByteArray();
        this.buf = null;
        return capture;
    }

// PrintStream Methods

    @Override
    public void close() {
        synchronized (this) {
            this.buf = null;
        }
        super.close();
    }

    @Override
    public void write(int b) {
        super.write(b);
        synchronized (this) {
            if (this.buf != null)
                this.buf.write(b);
        }
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        synchronized (this) {
            if (this.buf != null)
                this.buf.write(buf, off, len);
        }
    }
}
