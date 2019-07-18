package com.sshakshin.isoknife.containers;

import com.sshakshin.isoknife.containers.readers.FixedInputStream;
import com.sshakshin.isoknife.containers.readers.PreEditInputStream;
import com.sshakshin.isoknife.containers.readers.RDWInputStream;
import com.sshakshin.isoknife.util.AppConfig;

import java.io.*;

public abstract class Container {

    public static InputStream getInputStream() throws FileNotFoundException {
        InputStream clean = new BufferedInputStream(new FileInputStream(AppConfig.get().source));
        switch (AppConfig.get().container) {
            case CLEAN:
                return clean;
            case RDW:
                return new RDWInputStream(clean, false);
            case FIXED1014:
                return new RDWInputStream(new FixedInputStream(clean), true);
            case PREEDIT:
                return new PreEditInputStream(clean);
        }
        return null;
    }

    public static OutputStream getOutputStream() {
        OutputStream res = null;
        switch (AppConfig.get().container) {

        }
        return res;
    }
}
