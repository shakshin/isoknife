package com.sshakshin.isoknife.iso8583;

import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.Tracer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

public class IsoMessage {

    private String mti = null;
    private ArrayList<Integer> fieldSet = new ArrayList<Integer>();
    private List<IsoField> fields = new ArrayList<IsoField>();

    private static byte[] readBitmap(InputStream in) throws IOException {
        Tracer.log("IsoMessage", "Reading bitmap");
        byte[] bm = new byte[8];
        int cnt = in.read(bm);
        if (cnt < 8) {
            Tracer.log("IsoMessage", "No enough bytes to read bitmap");
            throw new IOException("No enough bytes to read bitmap");
        }
        return bm;
    }

    private boolean parseBitmap(byte[] buff, int index) {
        Tracer.log("IsoMessage", "Parsing bitmap");
        boolean hasNextBitmap = false;
        for (int byteIdx = 0; byteIdx < 8; byteIdx++) {
            BitSet bs = BitSet.valueOf(new byte[] { buff[byteIdx]});
            for (int bitIdx = 0; bitIdx < 8; bitIdx++) {
                int fieldIdx =
                        ((index - 1) * 64) // bitmap number
                        + (byteIdx * 8) // byte number
                        + (bitIdx + 1);

                if (bs.get(7-bitIdx)) {
                    if (fieldIdx == ((index - 1) * 64) + 1) {
                        Tracer.log("IsoMessage", "Has next bitmap");
                        hasNextBitmap = true;
                    } else {
                        fieldSet.add(fieldIdx);
                    }
                }
            }
        }

        return hasNextBitmap;
    }

    private static String readMti(InputStream in) throws IOException {
        Tracer.log("IsoMessage", "Reading MTI");
        byte[] buff = new byte[4];
        int cnt = in.read(buff);
        if (cnt <= 0) { // end of stream ?
            Tracer.log("IsoMessage", "No bytes for MTI. End of stream.");
            return null;
        }
        if (cnt < 4) {
            Tracer.log("IsoMessage", "No enough bytes for MTI");
            throw new IOException("No enough bytes to read MTI");
        }
        Tracer.log("IsoMessage", "MTI read ok");
        return new String(buff, AppConfig.get().getRawCharset());
    }

    public static IsoMessage parse(InputStream in) throws IOException {
        Tracer.log("IsoMessage", "Parsing message");
        IsoMessage msg = new IsoMessage();

        msg.setMti(readMti(in));
        if (msg.getMti() == null) {
            Tracer.log("IsoMessage", "No MTI read. End of stream.");
            return null;
        }

        int bmIdx = 1;
        Tracer.log("IsoMessage", "Getting bitmap(s)");
        while (true) {
            byte[] rawBm = readBitmap(in);
            boolean hasNextBm = msg.parseBitmap(rawBm, bmIdx);
            if (!hasNextBm)
                break;

            bmIdx++;
        }

        Tracer.log("IsoMessage", "Bitmap(s) parsing finished");

        IsoMessageDefinition mdef = AppConfig.get().getMesssageDef();
        Tracer.log("IsoMessage", "Got message structure definition");
        if (mdef == null) {
            Tracer.log("IsoMessage", "No message definition. Don't know how to parse");
            throw new IOException("No message definition found");
        }

        Map<Integer, IsoFieldDefinition> fdefs = mdef.getFields();

        for (Integer fIdx : msg.fieldSet) {
            IsoFieldDefinition fdef = fdefs.get(fIdx);
            if (fdef == null) {
                Tracer.log("IsoMessage", "No such field definition: " + fIdx.toString());
                throw new IOException("No field definition for index " + fIdx.toString());
            }
            Tracer.log("IsoMessage", "Got field definition");

            IsoField fld = IsoField.read(in, fdef);
            Tracer.log("IsoMessage", "Field parsed");

            msg.getFields().add(fld);
        }

        Tracer.log("IsoMessage", "Message parsed");
        return msg;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public List<IsoField> getFields() {
        return fields;
    }

    public void setFields(List<IsoField> fields) {
        this.fields = fields;
    }
}
