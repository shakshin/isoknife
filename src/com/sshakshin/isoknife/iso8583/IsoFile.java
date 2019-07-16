package com.sshakshin.isoknife.iso8583;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class IsoFile {

    private LinkedList<IsoMessage> messages = new LinkedList<>();


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

    public static IsoFile load(String source) {
        return null;
    }

    public void save(String dst) {

    }


    public LinkedList<IsoMessage> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<IsoMessage> messages) {
        this.messages = messages;
    }
}
