package com.sshakshin.isoknife.iso8583;

public class IsoFieldDefinition {
    public enum LengthType { FIXED, EMBEDDED }

    public Integer index;
    public LengthType lengthType;
    public Integer length;
    public boolean binary;

    public IsoFieldDefinition(int idx, LengthType ltype, int len, boolean bin) {
        index = idx;
        lengthType = ltype;
        length = len;
        binary = bin;
    }

}
