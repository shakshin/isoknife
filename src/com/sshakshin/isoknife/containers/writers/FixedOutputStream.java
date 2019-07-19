package com.sshakshin.isoknife.containers.writers;

import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.OutputStream;

public class FixedOutputStream extends OutputStream {

    private OutputStream out = null;
    private int counter = 0;
    private byte[] trailer = new byte[] { 0x00, 0x00 };

    public FixedOutputStream(OutputStream _out) {
        super();
        out = _out;
        Tracer.log("Fixed1014 out", "Stream created");
    }

    @Override
    public void write(int i) throws IOException {
        out.write(i);
        counter++;
        if (counter == 1012) {
            Tracer.log("Fixed1014 out", "Time to write block trailer");
            out.write(trailer);
            counter = 0;
        }
    }

    @Override
    public void close() throws IOException {
        Tracer.log("Fixed1014 out", "Aligning output file to block size");
        byte[] filler = new byte[] { 0x00 };
        while (counter < 1012) {
            out.write(filler);
            counter++;
        }
        out.write(trailer);
        super.close();
    }

}
