package com.sshakshin.isoknife.containers.readers;

import com.sshakshin.isoknife.util.DataConverter;
import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class RDWInputStream extends InputStream {

    private InputStream in = null;
    private byte[] buffer = null;
    private int offset = 0;
    private boolean inFixed = false;

    private String hexBuff = null;

    public RDWInputStream(InputStream i, boolean fixed) {
        super();
        in = i;
        inFixed = fixed;
        Tracer.log("RDW in", "Stream created");
    }

    private void fetchMessage() throws IOException {
        Tracer.log("RDW in", "Time to fetch next message from parent stream");
        offset = 0;
        byte[] rdw = new byte[4];
        int cnt = in.read(rdw);
        if (cnt <= 0) { // end of stream
            Tracer.log("RDW in", "End of parent stream reached");
            buffer = null;
            return;
        }

        if (cnt < 4 && !inFixed) {
            Tracer.log("RDW in", "No enough byte to read RDW header: " + String.valueOf(cnt));
            throw new IOException("No enough bytes to read RDW header");
        } else if (cnt < 4 && inFixed) {
            Tracer.log("RDW in", "End of parent stream reached");
            buffer = null;
            return;
        }

        Tracer.log("RDW in", "RDW header fetched");

        ByteBuffer bb = ByteBuffer.wrap(rdw);
        int lengthRdw = bb.getInt();

        Tracer.log("RDW in", "RDW header parsed. Message length: " + String.valueOf(lengthRdw));

        if (lengthRdw == 0) {
            Tracer.log("RDW in", "Zero-sized message. End of stream.");
            buffer = null;
            return;
        }

        buffer = new byte[lengthRdw];
        int lengthReal = in.read(buffer);

        hexBuff = DataConverter.bytesToHex(buffer);

        Tracer.log("RDW in", "Message payload fetched");

        if (lengthReal < lengthRdw) {
            Tracer.log("RDW in", "Actual message length: " + String.valueOf(lengthReal));
            throw new IOException(String.format("No enough bytes to read message payload. Length id RDW header: %d bytes; Actual length: %d bytes", lengthRdw, lengthReal));
        }

    }

    @Override
    public int read() throws IOException {
        if (buffer == null || offset == buffer.length) {
            fetchMessage();
        }

        if (buffer == null) return -1;

        return buffer[offset++];
    }


}
