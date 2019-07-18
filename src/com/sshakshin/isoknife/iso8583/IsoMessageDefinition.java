package com.sshakshin.isoknife.iso8583;

import java.util.Map;

public abstract class IsoMessageDefinition {
    public abstract Map<Integer, IsoFieldDefinition> getFields();
    public abstract void postProcess(IsoMessage message);
}
