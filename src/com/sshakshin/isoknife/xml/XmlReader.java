package com.sshakshin.isoknife.xml;

import com.sshakshin.isoknife.iso8583.IsoField;
import com.sshakshin.isoknife.iso8583.IsoFile;
import com.sshakshin.isoknife.iso8583.IsoMessage;
import com.sshakshin.isoknife.util.DataConverter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class XmlReader {

    public static IsoFile read(String source) throws IOException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new BufferedInputStream(new FileInputStream(new File(source))));
            Node root = doc.getDocumentElement();
            if (!root.getNodeName().equals("IsoFile")) {
                throw new IOException("Wrong root node name");
            }

            IsoFile isoFile = new IsoFile();

            NodeList messages = root.getChildNodes();

            for (int messageIndex = 0; messageIndex < messages.getLength(); messageIndex++) {
                Node message = messages.item(messageIndex);
                if (!message.getNodeName().equals("IsoMessage")) continue;

                IsoMessage isoMessage = new IsoMessage();
                {
                    NamedNodeMap attrs = message.getAttributes();
                    for (int attributeIndex = 0; attributeIndex < attrs.getLength(); attributeIndex++) {
                        Node attr = attrs.item(attributeIndex);
                        switch (attr.getNodeName()) {
                            case "Mti":
                                isoMessage.setMti(attr.getNodeValue());
                                break;
                        }
                    }
                }

                NodeList fields = message.getChildNodes();

                for (int fieldIndex = 0; fieldIndex < fields.getLength(); fieldIndex++) {
                    Node field = fields.item(fieldIndex);
                    if (!field.getNodeName().equals("IsoField")) continue;

                    String name = null;
                    String hex = null;
                    String val = null;

                    NamedNodeMap attrs = field.getAttributes();
                    for (int attributeIndex = 0; attributeIndex < attrs.getLength(); attributeIndex++) {
                        Node attr = attrs.item(attributeIndex);
                        switch (attr.getNodeName()) {
                            case "Name":
                                name = attr.getNodeValue();
                                break;
                            case "Value":
                                val = attr.getNodeValue();
                                break;
                        }
                    }

                    for (int k = 0; k < field.getChildNodes().getLength(); k ++) {
                        Node sub = field.getChildNodes().item(k);
                        switch (sub.getNodeName()) {
                            case "Binary":
                                hex = sub.getChildNodes().item(0).getNodeValue();
                                break;
                        }
                    }

                    if (name == null)
                        throw new IOException("No field name defined");

                    if (val == null && hex == null)
                        throw new IOException("No field value defined");

                    IsoField isoField = new IsoField();
                    isoField.setIndex(Integer.parseInt(name));
                    if (hex != null) {
                        isoField.setRawData(DataConverter.hexToBytes(hex));
                    } else {
                        isoField.setData(val);
                    }

                    isoMessage.getFields().add(isoField);


                }


                isoFile.getMessages().add(isoMessage);
            }

            return isoFile;


        } catch (Exception e) {
            throw new IOException("Can not read XML", e);
        }
    }
}
