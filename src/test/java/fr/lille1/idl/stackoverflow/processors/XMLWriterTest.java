package fr.lille1.idl.stackoverflow.processors;

import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by dorian on 30/11/14.
 */
public class XMLWriterTest {
    @Test
    public void testXMLIsValid() throws IOException, XMLStreamException {
        File file = File.createTempFile("test", "xml");
        String filename = System.getProperty("java.io.tmpdir") + File.separator + "so-exctractor.xml";
        try {
            XMLWriter writer = new XMLWriter(file.getAbsolutePath());
            writer.close();
        } catch (XMLStreamException unexpected) {
            fail(unexpected.getMessage());
        }
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        FileInputStream fis = new FileInputStream(file);
        XMLEventReader reader = inputFactory.createXMLEventReader(fis);
        //Read XML header
        reader.nextEvent();
        XMLEvent openingPostTag = reader.nextEvent();
        assertTrue(openingPostTag.isStartElement());
        assertEquals(openingPostTag.asStartElement().getName().toString(), "posts");
        XMLEvent closingPostTag = reader.nextEvent();
        assertTrue(closingPostTag.isEndElement());
        assertEquals(closingPostTag.asEndElement().getName().toString(), "posts");
        assertEquals("enddocument", reader.nextEvent().toString().toLowerCase());
        reader.close();
        fis.close();
        file.delete();
    }
}
