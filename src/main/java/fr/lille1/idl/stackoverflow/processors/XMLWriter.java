package fr.lille1.idl.stackoverflow.processors;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by dorian on 21/11/14.
 */
public class XMLWriter implements XMLEventProcessor {
    private XMLEventFactory eventFactory;
    private XMLEventWriter writer;

    public XMLWriter(final String filename) throws FileNotFoundException, XMLStreamException {
        OutputStream os = new FileOutputStream(filename);
        XMLOutputFactory xof = XMLOutputFactory.newFactory();
        this.writer = xof.createXMLEventWriter(os);
        this.eventFactory = XMLEventFactory.newFactory();
        XMLEvent startElement = this.eventFactory.createStartElement(new QName("posts"), null, null);
        this.writer.add(startElement);
    }

    @Override
    public void process(final XMLEvent event) throws XMLStreamException {
        this.writer.add(event);
        this.writer.add(eventFactory.createEndElement(new QName("row"), null));
    }

    public void close() throws XMLStreamException {
        if (writer != null) {
            XMLEvent endElement = this.eventFactory.createEndElement(new QName("posts"), null);
            this.writer.add(endElement);
            this.writer.flush();
            this.writer.close();
        }
    }
}
