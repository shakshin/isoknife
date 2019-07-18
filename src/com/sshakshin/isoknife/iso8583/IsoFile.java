package com.sshakshin.isoknife.iso8583;

import com.sshakshin.isoknife.xml.XmlReader;
import com.sshakshin.isoknife.xml.XmlWriter;

import java.io.*;
import java.util.ArrayList;

public class IsoFile {

    private ArrayList<IsoMessage> messages = new ArrayList<>();


    public static IsoFile parse(InputStream in) throws IOException {
        IsoFile file = new IsoFile();

        IsoMessage msg = null;
        while(true) {
            msg = IsoMessage.parse(in);
            if (msg != null) {
                file.messages.add(msg);

            } else {
                break;
            }

        }

        return file;
    }

    public void merge(OutputStream out) {

    }

    public static IsoFile load(String source) throws IOException {
        IsoFile file = XmlReader.read(source);
        return file;
    }

    public void save(String dst) throws IOException {
        XmlWriter.write(this, dst);
    }


    public ArrayList<IsoMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<IsoMessage> messages) {
        this.messages = messages;
    }
}
