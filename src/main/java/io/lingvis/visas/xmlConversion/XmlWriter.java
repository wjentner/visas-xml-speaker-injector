package io.lingvis.visas.xmlConversion;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

class XmlWriter {

    private XMLStreamWriter writer;

    void openWriter(File file) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        FileOutputStream fos = new FileOutputStream(file);
        writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");
    }

    void closeWriter() throws XMLStreamException {
        writer.close();
    }

    void writeXmlEncoding() throws XMLStreamException {
        writer.writeStartElement("?xml version=\"1.0\" encoding=\"UTF-8\"?");
    }

    void writeTopicStart(String name) throws XMLStreamException {
        writer.writeStartElement("topic");
        writer.writeAttribute("name", name);
        writer.writeCharacters("\n");
    }

    void writeSectionStart() throws XMLStreamException {
        writer.writeStartElement("section");
       // writer.writeAttribute("name", name);
        writer.writeCharacters("\n");
    }

    void writeUtteranceStart(String id, String name) throws XMLStreamException {
        writer.writeStartElement("utterance");
        writer.writeAttribute("sprecherId", id);
        writer.writeAttribute("name", name);
        writer.writeCharacters("\n");
    }

    void writeText(String text) throws XMLStreamException {
        writer.writeCharacters(text);
    }

    public void writeEndElement() throws XMLStreamException {
        writer.writeCharacters("\n");
        writer.writeEndElement();
    }
}
