package com.sshakshin.isoknife.xml;

import com.sshakshin.isoknife.iso8583.IsoField;
import com.sshakshin.isoknife.iso8583.IsoFile;
import com.sshakshin.isoknife.iso8583.IsoMessage;
import com.sshakshin.isoknife.util.DataConverter;
import com.sshakshin.isoknife.util.Tracer;
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
        Tracer.log("XmlReader", "Reading XML representation");
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new BufferedInputStream(new FileInputStream(new File(source))));
            Node root = doc.getDocumentElement();
            if (!root.getNodeName().equals("IsoFile")) {
                Tracer.log("XmlReader", "Wrong root element: " + root.getNodeName());
                throw new IOException("Wrong root node name");
            }

            IsoFile isoFile = new IsoFile();

            NodeList messages = root.getChildNodes();

            Tracer.log("XmlReader", "Iterating message elements");

            for (int messageIndex = 0; messageIndex < messages.getLength(); messageIndex++) {
                Node message = messages.item(messageIndex);
                if (!message.getNodeName().equals("IsoMessage")) {
                    Tracer.log("XmlReader", "Wrong element name: " + message.getNodeName() + ". Skipping.");
                    continue;
                }

                IsoMessage isoMessage = new IsoMessage();
                {
                    Tracer.log("XmlReader", "Reading message attributes");
                    NamedNodeMap attrs = message.getAttributes();
                    for (int attributeIndex = 0; attributeIndex < attrs.getLength(); attributeIndex++) {
                        Node attr = attrs.item(attributeIndex);
                        switch (attr.getNodeName()) {
                            case "Mti":
                                Tracer.log("XmlReader", "MTI attribute found");
                                isoMessage.setMti(attr.getNodeValue());
                                break;
                            default:
                                Tracer.log("XmlReader", "Unknown attribute: " + attr.getNodeName());
                        }
                    }
                }

                Tracer.log("XmlReader", "Iterating fields elements");
                NodeList fields = message.getChildNodes();

                for (int fieldIndex = 0; fieldIndex < fields.getLength(); fieldIndex++) {
                    Node field = fields.item(fieldIndex);
                    if (!field.getNodeName().equals("IsoField")) {
                        Tracer.log("XmlReader", "Wrong element name: " + field.getNodeName());
                        continue;
                    }

                    String name = null;
                    String hex = null;
                    String val = null;

                    Tracer.log("XmlReader", "Reading field attributes");
                    NamedNodeMap attrs = field.getAttributes();
                    for (int attributeIndex = 0; attributeIndex < attrs.getLength(); attributeIndex++) {
                        Node attr = attrs.item(attributeIndex);
                        switch (attr.getNodeName()) {
                            case "Name":
                                Tracer.log("XmlReader", "Found field name");
                                name = attr.getNodeValue();
                                break;
                            case "Value":
                                Tracer.log("XmlReader", "Found field value");
                                val = attr.getNodeValue();
                                break;
                            default:
                                Tracer.log("XmlReader", "Unknown field attribute: " + attr.getNodeName());
                        }
                    }

                    Tracer.log("XmlReader", "Looking for child nodes of field (raw binary data)");
                    for (int k = 0; k < field.getChildNodes().getLength(); k ++) {
                        Node sub = field.getChildNodes().item(k);
                        switch (sub.getNodeName()) {
                            case "Binary":
                                Tracer.log("XmlReader", "Found binary data");
                                hex = sub.getChildNodes().item(0).getNodeValue();
                                break;
                        }
                    }

                    if (name == null) {
                        Tracer.log("XmlReader", "Field name undefined");
                        throw new IOException("No field name defined");
                    }

                    if (val == null && hex == null) {
                        Tracer.log("XmlReader", "Field value undefined");
                        throw new IOException("No field value defined");
                    }

                    IsoField isoField = new IsoField();
                    isoField.setIndex(Integer.parseInt(name));
                    if (hex != null) {
                        Tracer.log("XmlReader", "Using binary data");
                        isoField.setRawData(DataConverter.hexToBytes(hex));
                    } else {
                        Tracer.log("XmlReader", "Using readable value");
                        isoField.setData(val);
                    }

                    isoMessage.getFields().add(isoField);


                }


                isoFile.getMessages().add(isoMessage);
            }

            Tracer.log("XmlReader", "XML representation load ok");
            return isoFile;


        } catch (Exception e) {
            Tracer.log("XmlReader", "XML representation read failed");
            throw new IOException("Can not read XML", e);
        }
    }
}
