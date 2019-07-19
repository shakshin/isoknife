package com.sshakshin.isoknife.mastercard;

import com.sshakshin.isoknife.iso8583.IsoFieldDefinition;
import com.sshakshin.isoknife.iso8583.IsoMessage;
import com.sshakshin.isoknife.iso8583.IsoMessageDefinition;

import java.util.HashMap;
import java.util.Map;

public class IpmMessageDefinition extends IsoMessageDefinition {
    @Override
    public Map<Integer, IsoFieldDefinition> getFields() {
        HashMap<Integer, IsoFieldDefinition> res = new HashMap<>();

        res.put(2, new IsoFieldDefinition(2, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(3, new IsoFieldDefinition(3, IsoFieldDefinition.LengthType.FIXED, 6, false));
        res.put(4, new IsoFieldDefinition(4, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(5, new IsoFieldDefinition(5, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(6, new IsoFieldDefinition(6, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(9, new IsoFieldDefinition(9, IsoFieldDefinition.LengthType.FIXED, 8, false));
        res.put(10, new IsoFieldDefinition(10, IsoFieldDefinition.LengthType.FIXED, 8, false));
        res.put(12, new IsoFieldDefinition(12, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(14, new IsoFieldDefinition(14, IsoFieldDefinition.LengthType.FIXED, 4, false));
        res.put(22, new IsoFieldDefinition(22, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(23, new IsoFieldDefinition(23, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(24, new IsoFieldDefinition(24, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(25, new IsoFieldDefinition(25, IsoFieldDefinition.LengthType.FIXED, 4, false));
        res.put(26, new IsoFieldDefinition(26, IsoFieldDefinition.LengthType.FIXED, 4, false));
        res.put(30, new IsoFieldDefinition(30, IsoFieldDefinition.LengthType.FIXED, 24, false));
        res.put(31, new IsoFieldDefinition(31, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(32, new IsoFieldDefinition(32, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(33, new IsoFieldDefinition(33, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(37, new IsoFieldDefinition(37, IsoFieldDefinition.LengthType.FIXED, 12, false));
        res.put(38, new IsoFieldDefinition(38, IsoFieldDefinition.LengthType.FIXED, 6, false));
        res.put(40, new IsoFieldDefinition(40, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(41, new IsoFieldDefinition(41, IsoFieldDefinition.LengthType.FIXED, 8, false));
        res.put(42, new IsoFieldDefinition(42, IsoFieldDefinition.LengthType.FIXED, 15, false));
        res.put(43, new IsoFieldDefinition(43, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(48, new IsoFieldDefinition(48, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(49, new IsoFieldDefinition(49, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(50, new IsoFieldDefinition(50, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(51, new IsoFieldDefinition(51, IsoFieldDefinition.LengthType.FIXED, 3, false));
        res.put(54, new IsoFieldDefinition(54, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(55, new IsoFieldDefinition(55, IsoFieldDefinition.LengthType.EMBEDDED, 3, true));
        res.put(62, new IsoFieldDefinition(62, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(63, new IsoFieldDefinition(63, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(71, new IsoFieldDefinition(71, IsoFieldDefinition.LengthType.FIXED, 8, false));
        res.put(72, new IsoFieldDefinition(72, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(73, new IsoFieldDefinition(73, IsoFieldDefinition.LengthType.FIXED, 6, false));
        res.put(93, new IsoFieldDefinition(93, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(94, new IsoFieldDefinition(94, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(95, new IsoFieldDefinition(95, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(100, new IsoFieldDefinition(100, IsoFieldDefinition.LengthType.EMBEDDED, 2, false));
        res.put(111, new IsoFieldDefinition(111, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(123, new IsoFieldDefinition(123, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(124, new IsoFieldDefinition(124, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(125, new IsoFieldDefinition(125, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));
        res.put(127, new IsoFieldDefinition(127, IsoFieldDefinition.LengthType.EMBEDDED, 3, false));


        return res;
    }

    @Override
    public void postProcess(IsoMessage message) {

    }
}
