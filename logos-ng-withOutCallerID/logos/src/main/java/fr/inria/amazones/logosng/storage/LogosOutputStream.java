package fr.inria.amazones.logosng.storage;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LogosOutputStream extends BufferedOutputStream {

    private long length = 0;

    public LogosOutputStream(OutputStream o) throws Exception {
        super(o, 8192);
    }

    public void setOutputStream(OutputStream os) throws Exception {
        length = 0;
        this.out = os;
    }

    public long getCurrentLength() {
        return length;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        length = length + 1;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        length = length + (len - off);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        length = length + b.length;
    }
}
