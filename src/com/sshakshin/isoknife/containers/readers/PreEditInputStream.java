package com.sshakshin.isoknife.containers.readers;

import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PreEditInputStream extends InputStream {

    private InputStream in = null;
    private byte[] buffer = null;
    private int offset = 0;
    private boolean headerFetched = false;

    public PreEditInputStream(InputStream i) {
        super();
        in = i;
        Tracer.log("Pre-Edit in", "Stream created");
    }

    private void fetchFileHeader() throws IOException {
        Tracer.log("Pre-Edit in", "Time to fetch file header from parent stream");
        byte[] buff = new byte[128];
        int cnt = in.read(buff);
        if (cnt != 128) {
            Tracer.log("Pre-Edit in", "No enough bytes to read Pre-EDit file header");
            throw new IOException("No enough bytes to read file header");
        }
        headerFetched = true;
        Tracer.log("Pre-Edit in", "File header fetched");
    }

    private void fetchMessage() throws IOException {
        Tracer.log("Pre-Edit in", "Time to fetch next message from parent stream");

        if (!headerFetched)
            fetchFileHeader();

        offset = 0;
        byte[] rdw = new byte[4];
        int cnt = in.read(rdw);
        if (cnt <= 0) { // end of stream
            Tracer.log("Pre-Edit in", "End of parent stream reached");
            buffer = null;
            return;
        }

        if (cnt < 4) {
            Tracer.log("Pre-Edit in", "No enough byte to read Pre-Edit header: " + String.valueOf(cnt));
            throw new IOException("No enough bytes to read Pre-Edit header");
        }

        Tracer.log("Pre-Edit in", "Pre-Edit header fetched");

        if (rdw[0] != 64) {
            Tracer.log("Pre-Edit in", "Pre-Edit header format error");
            throw new IOException("Pre-Edit header format error");
        }
        rdw[0] = 0;

        ByteBuffer bb = ByteBuffer.wrap(rdw);
        int lengthRdw = bb.getInt();

        Tracer.log("Pre-Edit in", "Pre-Edit header parsed. Message length: " + String.valueOf(lengthRdw));

        if (lengthRdw == 0) {
            Tracer.log("Pre-Edit in", "Zero-sized message. End of stream.");
            buffer = null;
            return;
        }

        buffer = new byte[lengthRdw];
        int lengthReal = in.read(buffer);

        Tracer.log("Pre-Edit in", "Message payload fetched");

        if (lengthReal < lengthRdw) {
            Tracer.log("Pre-Edit in", "Actual message length: " + String.valueOf(lengthReal));
            throw new IOException(String.format("No enough bytes to read message payload. Length id Pre-Edit header: %d bytes; Actual length: %d bytes", lengthRdw, lengthReal));
        }

    }

    @Override
    public int read() throws IOException {
        if (buffer == null || offset == buffer.length - 1) {
            fetchMessage();
        }

        if (buffer == null) return -1;

        return buffer[offset++];
    }


}
