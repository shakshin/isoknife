package com.sshakshin.isoknife.xml;

import com.sshakshin.isoknife.iso8583.IsoField;
import com.sshakshin.isoknife.iso8583.IsoFile;
import com.sshakshin.isoknife.iso8583.IsoMessage;
import com.sshakshin.isoknife.util.DataConverter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class XmlWriter {

    private static DocumentBuilder db;
    private static Document doc;

    public static void write(IsoFile file, String destination) throws IOException {
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = db.newDocument();
            Element xfile =  doc.createElement("IsoFile");

            writeMessages(file, xfile);

            doc.appendChild(xfile);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            tr.transform(new DOMSource(doc),
                    new StreamResult(new FileOutputStream(new File(destination))));
        } catch (Exception e) {
            throw new IOException("Can not write XML" , e);
        }
    }

    public static void writeMessages(IsoFile file, Element root) {
        for (IsoMessage mess : file.getMessages()) {
            Node xmess = doc.createElement("IsoMessage");
            ((Element)xmess).setAttribute("Mti", mess.getMti());

            writeFields(mess, xmess);

            root.appendChild(xmess);
        }
    }

    public static void writeFields(IsoMessage mess, Node root) {

        for (IsoField fld : mess.getFields()) {

            Node xfield = doc.createElement("IsoField");

            ((Element)xfield).setAttribute("Name", fld.getIndex().toString());

            if (fld.isBinary()) {
                Node xdata = doc.createElement("Binary");
                xdata.appendChild(doc.createTextNode(DataConverter.bytesToHex(fld.getRawData())));
                xfield.appendChild(xdata);
            } else {
                ((Element)xfield).setAttribute("Value", fld.getData());
            }

            root.appendChild(xfield);

        }

    }
}
