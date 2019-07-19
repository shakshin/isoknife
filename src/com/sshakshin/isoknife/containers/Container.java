package com.sshakshin.isoknife.containers;

import com.sshakshin.isoknife.containers.readers.FixedInputStream;
import com.sshakshin.isoknife.containers.readers.PreEditInputStream;
import com.sshakshin.isoknife.containers.readers.RDWInputStream;
import com.sshakshin.isoknife.containers.writers.FixedOutputStream;
import com.sshakshin.isoknife.containers.writers.RDWOutputStream;
import com.sshakshin.isoknife.util.AppConfig;
import com.sshakshin.isoknife.util.Tracer;

import java.io.*;

public abstract class Container {

    public static InputStream getInputStream() throws FileNotFoundException {
        InputStream clean = new BufferedInputStream(new FileInputStream(AppConfig.get().source));
        switch (AppConfig.get().container) {
            case CLEAN:
                Tracer.log("Container", "Selected clean file reader");
                return clean;
            case RDW:
                Tracer.log("Container", "Selected RDW file reader");
                return new RDWInputStream(clean, false);
            case FIXED1014:
                Tracer.log("Container", "Selected Fixed1014 file reader");
                return new RDWInputStream(new FixedInputStream(clean), true);
            case PREEDIT:
                Tracer.log("Container", "Selected pre-edit file reader");
                return new PreEditInputStream(clean);
        }
        return null;
    }

    public static OutputStream getOutputStream() throws IOException {
        OutputStream clean = new FileOutputStream(AppConfig.get().destination);
        switch (AppConfig.get().container) {
            case CLEAN:
                Tracer.log("Container", "Selected  clean file writer");
                return clean;
            case RDW:
                Tracer.log("Container", "Selected RDW file writer");
                return new RDWOutputStream(clean);
            case FIXED1014:
                Tracer.log("Container", "Selected Fixed1014 file writer");
                return new RDWOutputStream(new FixedOutputStream(clean));
            case PREEDIT:
                Tracer.log("Container", "Selected Pre-Edit file writer (unsupported)");
                throw new IOException("Pre-Edit writing is not supported");
        }
        throw new IOException("Unsupported writer specified");
    }
}
