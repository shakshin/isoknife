package com.sshakshin.isoknife.iso8583;

import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.DataConverter;

import java.io.IOException;
import java.io.InputStream;

public class IsoField {
    private Integer index;
    private byte[] rawData;
    private String data;
    private boolean binary;

    public static IsoField read(InputStream in, IsoFieldDefinition def) throws IOException {
        IsoField field = new IsoField();
        field.setIndex(def.index);

        byte[] buff = null;

        try {
            switch (def.lengthType) {
                case FIXED:
                    buff = readFixed(in, def.length);

                    break;
                case EMBEDDED:
                    buff = readEmbedded(in, def.length);
                    break;
            }
        } catch (IOException e) {
            throw new IOException("Can not read field " + field.getIndex().toString(), e);
        }

        field.setRawData(def.binary ? buff : DataConverter.convertBytesOnRead(buff, AppConfig.get().getRawCharset()));
        if (!def.binary) {
            field.setData(new String(field.getRawData()));
        }
        field.setBinary(def.binary);
        return field;
    }

    private static byte[] readFixed(InputStream in, int len) throws IOException {
        byte[] buff = new byte[len];
        int cnt = in.read(buff);
        if (cnt != len)
            throw new IOException("No enough bytes");

        return buff;
    }

    private static byte[] readEmbedded(InputStream in, int len) throws IOException {
        byte[] rawLen = null;
        try {
            rawLen = readFixed(in, len);
        } catch (IOException e) {
            throw new IOException("Can not read field length", e);
        }

        Integer flen = Integer.parseInt(new String(DataConverter.convertBytesOnRead(rawLen, AppConfig.get().getRawCharset())));
        if (flen > 0) {
            try {
                return readFixed(in, flen);
            } catch (IOException e) {
                throw new IOException("Can not read field data", e);
            }
        } else {
            return new byte[0];
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }


    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }
}
