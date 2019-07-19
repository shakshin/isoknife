package com.sshakshin.isoknife.iso8583;

import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.DataConverter;
import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class IsoField {
    private Integer index;
    private byte[] rawData;
    private String data;
    private boolean binary;

    public static IsoField read(InputStream in, IsoFieldDefinition def) throws IOException {
        Tracer.log("IsoField", "Reading field " + def.index.toString());
        IsoField field = new IsoField();
        field.setIndex(def.index);

        byte[] buff = null;

        try {
            switch (def.lengthType) {
                case FIXED:
                    Tracer.log("IsoField", "Fixed length field");
                    buff = readFixed(in, def.length);
                    break;
                case EMBEDDED:
                    Tracer.log("IsoField", "Embedded length field");
                    buff = readEmbedded(in, def.length);
                    break;
            }
        } catch (IOException e) {
            Tracer.log("IsoField", "Error reading field: " + e.getMessage());
            throw new IOException("Can not read field " + field.getIndex().toString(), e);
        }

        field.setRawData(def.binary ? buff : DataConverter.convertBytesOnRead(buff, AppConfig.get().getRawCharset()));
        if (!def.binary) {
            Tracer.log("IsoField", "Parsed string data representation");
            field.setData(new String(field.getRawData()));
        }
        field.setBinary(def.binary);
        return field;
    }

    private static byte[] readFixed(InputStream in, int len) throws IOException {
        byte[] buff = new byte[len];
        int cnt = in.read(buff);
        if (cnt != len) {
            Tracer.log("IsoField", "No enough bytes to read next data potion");
            throw new IOException("No enough bytes");
        }
        return buff;
    }

    private static byte[] readEmbedded(InputStream in, int len) throws IOException {
        byte[] rawLen = null;
        try {
            Tracer.log("IsoField", "Reading embedded length");
            rawLen = readFixed(in, len);
        } catch (IOException e) {
            Tracer.log("IsoField", "Failed to read embedded length");
            throw new IOException("Can not read field length", e);
        }

        Integer flen = Integer.parseInt(new String(DataConverter.convertBytesOnRead(rawLen, AppConfig.get().getRawCharset())));
        Tracer.log("IsoField", "Embedded length parsed");
        if (flen > 0) {
            try {
                Tracer.log("IsoField", "Reading value");
                return readFixed(in, flen);
            } catch (IOException e) {
                Tracer.log("IsoField", "Failed to read value");
                throw new IOException("Can not read field data", e);
            }
        } else {
            return new byte[0];
        }
    }

    public void merge(OutputStream out) throws IOException {
        Tracer.log("IsoField", "Merging field " + index.toString());
        IsoMessageDefinition mdef = AppConfig.get().getMesssageDef();
        if (mdef == null) {
            Tracer.log("IsoField", "Failed to get message definition");
            throw new IOException("No message definition");
        }
        IsoFieldDefinition fdef = mdef.getFields().get(index);
        if (fdef == null) {
            Tracer.log("IsoField", "Failed to get field definition");
            throw new IOException("No field definition");
        }

        byte[] buff = null;
        if (rawData != null) {
            Tracer.log("IsoField", "Will use predefined RAW data");
            buff = rawData;
        } else if (data != null) {
            Tracer.log("IsoField", "Preparing field data");
            buff = data.getBytes(AppConfig.get().getRawCharset());
        }

        if (fdef.lengthType == IsoFieldDefinition.LengthType.EMBEDDED) {
            Tracer.log("IsoField", "Writing embedded length value");
            Integer len = buff.length;
            String slen = len.toString();
            if (slen.length() > fdef.length) {
                Tracer.log("IsoField", "Embedded length value is too long");
                throw new IOException("Field length value exceeds defined size");
            }
            while (slen.length() < fdef.length) slen = "0" + slen;

            out.write(slen.getBytes(AppConfig.get().getRawCharset()));

        } else {
            if (buff.length != fdef.length) {
                Tracer.log("IsoField", "Field value has wrong length. Should be  " + fdef.length);
                throw new IOException("Wrong field value length");
            }
        }

        out.write(buff);
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
