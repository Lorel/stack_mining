package fr.lille1.idl.stackoverflow;

import fr.lille1.idl.stackoverflow.processors.SQLProcessor;
import fr.lille1.idl.stackoverflow.processors.XMLEventProcessor;
import fr.lille1.idl.stackoverflow.processors.XMLWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorian on 19/11/14.
 */
public class Main {
    public static void main(final String[] args) throws Exception {
        long beginning = System.nanoTime();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        InputStream is = new FileInputStream("/home/dorian/IDL/StackOverflow/posts-filtered.xml");
        XMLEventReader reader = factory.createXMLEventReader(is);
        List<XMLEventProcessor> processors = new ArrayList<XMLEventProcessor>();
        int counter = 0;
        processors.add(new SQLProcessor());
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    counter++;
                    StartElement start = event.asStartElement();
                    QName startName = start.getName();
                    if (!startName.toString().trim().equalsIgnoreCase("row")) {
                        continue;
                    }
                    Attribute acceptedAnswer = start.getAttributeByName(new QName("AcceptedAnswerId"));
                    Attribute parentId = start.getAttributeByName(new QName("parentId"));
                    Attribute tags = start.getAttributeByName(new QName("Tags"));
                    if (acceptedAnswer != null && parentId == null && tags.toString().contains("java")) {
                        for (XMLEventProcessor processor : processors) {
                            try {
                                processor.process(event);
                            } catch (Exception processingException) {
                                continue;
                            }
                        }
                    }
                }
                if ((counter % 1000) == 0) {
                    System.out.println("counter : " + counter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            is.close();
            for (XMLEventProcessor processor : processors) {
                processor.close();
            }

        }
        long ending = System.nanoTime();
        System.out.println("Time spent : " + (ending - beginning) + " ns");
    }
}
