package fr.lille1.idl.stackoverflow.filters;

import junit.framework.TestCase;
import org.junit.Test;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

/**
 * Created by dorian on 01/12/14.
 */
public class JavaFilterTest {
    @Test
    public void test() throws XMLStreamException, IOException {
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        FileInputStream fis = new FileInputStream("src/main/resources/missingTagsAttribute.xml");
        XMLEventReader reader = inputFactory.createXMLEventReader(fis);
        XMLEvent row = reader.nextTag();
        JavaFilter filter = new JavaFilter();
        assertFalse(filter.test(row));
        fis.close();
    }
}
