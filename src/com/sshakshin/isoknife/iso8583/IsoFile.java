package com.sshakshin.isoknife.iso8583;

import com.sshakshin.isoknife.util.Tracer;
import com.sshakshin.isoknife.xml.XmlReader;
import com.sshakshin.isoknife.xml.XmlWriter;

import java.io.*;
import java.util.ArrayList;

public class IsoFile {

    private ArrayList<IsoMessage> messages = new ArrayList<>();

    public static IsoFile parse(InputStream in) throws IOException {
        Tracer.log("IsoFile", "ISO file parsing started");

        IsoFile file = new IsoFile();

        IsoMessage msg = null;
        while(true) {
            msg = IsoMessage.parse(in);
            if (msg != null) {
                Tracer.log("IsoFile", "Message parsed, adding to messages set");
                file.messages.add(msg);

            } else {
                break;
            }

        }
        Tracer.log("IsoFile", "ISO file parsing finished");
        return file;
    }

    public void merge(OutputStream out) throws IOException {
        Tracer.log("IsoFile", "ISO file merging started");
        for (IsoMessage message : messages) {
            message.merge(out);
        }
        out.flush();
        out.close();
        Tracer.log("IsoFile", "ISO file merging finished");
    }

    public static IsoFile load(String source) throws IOException {
        Tracer.log("IsoFile", "ISO file loading from XML started");
        IsoFile file = XmlReader.read(source);
        Tracer.log("IsoFile", "ISO file loading from XML finished");
        return file;
    }

    public void save(String dst) throws IOException {
        Tracer.log("IsoFile", "ISO file writing to XML started");
        XmlWriter.write(this, dst);
        Tracer.log("IsoFile", "ISO file writing to XML finished");
    }


    public ArrayList<IsoMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<IsoMessage> messages) {
        this.messages = messages;
    }
}
