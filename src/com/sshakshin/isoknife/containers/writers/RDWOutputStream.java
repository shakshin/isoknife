package com.sshakshin.isoknife.containers.writers;

import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class RDWOutputStream extends OutputStream {

    private OutputStream out = null;

    public RDWOutputStream(OutputStream _out) {
        super();
        out = _out;
        Tracer.log("RDW out", "Stream created");
    }

    @Override
    public void write(int i) throws IOException {
        out.write(i);
    }

    public void writeHeader(byte[] message) throws IOException {
        Tracer.log("RDW out", "Writing RDW header");
        int len = message.length;
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(len);
        out.write(bb.array());
    }

    @Override
    public void close() throws IOException {
        out.close();
        super.close();
    }
}
