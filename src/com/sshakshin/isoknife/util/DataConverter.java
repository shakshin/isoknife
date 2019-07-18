package com.sshakshin.isoknife.util;

import java.nio.charset.Charset;

public  abstract class DataConverter {

    public static String bytesToHex(byte[] input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            if (i > 0)
                sb.append(" ");

            sb.append((String.format("%x", input[i])));
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String input) {
        byte[] res = new byte[input.replace(" ", "").length() / 2];
        int offset = 0;
        for (String c : input.split(" ")) {
            res[offset++] = Integer.decode("0x" + c).byteValue();
        }
        return res;
    }

    public static byte[] convertBytesOnRead(byte[] src, Charset from) {
        if (from == Charset.forName("ASCII"))
            return src;

        return new String(src, from).getBytes(Charset.forName("ASCII"));
    }
}
