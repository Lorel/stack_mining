package fr.lille1.idl.stackoverflow;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.processors.SQLProcessor;
import fr.lille1.idl.stackoverflow.processors.XMLEventProcessor;

import javax.xml.namespace.QName;
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
    public static final String USAGE = "Usage : java -jar so-extractor-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/Posts.xml";

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        String filename = args[0];
        long beginning = System.nanoTime();
        FileInputStream fis = new FileInputStream("config.properties");
        Configuration configuration = Configuration.getConfiguration();
        configuration.load(fis);
        fis.close();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        InputStream is = new FileInputStream(filename);
        XMLEventReader reader = factory.createXMLEventReader(is);
        List<XMLEventProcessor> processors = new ArrayList<XMLEventProcessor>();
        int nodesCounter = 0, postsCounter = 0, interestingPostsCounter = 0;
        processors.add(new SQLProcessor());
        try {
            while (reader.hasNext()) {
                if ((nodesCounter % 1000) == 0) {
                    System.out.println("nodes processed : " + nodesCounter + ", posts processed : " + postsCounter + ", posts with stack traces : " + interestingPostsCounter);
                }
                XMLEvent event = reader.nextEvent();
                nodesCounter++;
                if (event.isStartElement()) {
                    postsCounter++;
                    StartElement start = event.asStartElement();
                    QName startName = start.getName();
                    if (!startName.toString().trim().equalsIgnoreCase("row")) {
                        continue;
                    }
                    Attribute acceptedAnswer = start.getAttributeByName(new QName("AcceptedAnswerId"));
                    Attribute parentId = start.getAttributeByName(new QName("parentId"));
                    Attribute tags = start.getAttributeByName(new QName("Tags"));
                    if (acceptedAnswer != null && parentId == null && tags.toString().contains("java")) {
                        Post post = new Post(event);
                        List<StackTrace> traces = StackTraceParser.parseAll(post.getBody());
                        if (traces.isEmpty()) {
                            continue;
                        }
                        interestingPostsCounter++;
                        for (XMLEventProcessor processor : processors) {
                            try {
                                processor.process(event);
                            } catch (Exception processingException) {
                                continue;
                            }
                        }
                    }
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
