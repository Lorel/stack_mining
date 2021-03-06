package fr.lille1.idl.stackoverflow;

import fr.lille1.idl.stackoverflow.filters.JavaFilter;
import fr.lille1.idl.stackoverflow.filters.XMLEventFilter;
import fr.lille1.idl.stackoverflow.processors.SQLProcessor;
import fr.lille1.idl.stackoverflow.processors.XMLEventProcessor;
import fr.lille1.idl.stackoverflow.tables.Post;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
        Logger logger = Logger.getGlobal();
        SimpleFormatter formatter = new SimpleFormatter();
        FileHandler fh = new FileHandler("out.log", true);
        fh.setFormatter(formatter);
        logger.addHandler(fh);
        String filename = args[0];
        long beginning = System.nanoTime();
        FileInputStream configFileInputStream = null;
        try {
            configFileInputStream = new FileInputStream("config.properties");
            Configuration configuration = Configuration.getConfiguration();
            configuration.load(configFileInputStream);
            configFileInputStream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not find config file config.properties", e);
            System.exit(1);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            System.exit(2);
        } finally {
            if (configFileInputStream != null) {
                configFileInputStream.close();
            }
        }
        XMLInputFactory factory = XMLInputFactory.newFactory();
        InputStream xmlInputStream = null;
        try {
            xmlInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not find input XML file", e);
            System.exit(3);
        }
        XMLEventReader reader = null;
        try {
            reader = factory.createXMLEventReader(xmlInputStream);
        } catch (XMLStreamException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            System.exit(4);
        }
        List<XMLEventProcessor> processors = new ArrayList<XMLEventProcessor>();
        int nodesCounter = 0, postsCounter = 0, interestingPostsCounter = 0;
        try {
            processors.add(new SQLProcessor());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Could not load JDBC driver", e);
            System.exit(5);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not initialize SQL prepared statement", e);
            System.exit(6);
        }
        XMLEventFilter filter = new JavaFilter();
        try {
            while (reader.hasNext()) {
                if ((nodesCounter % 1000) == 0) {
                    logger.log(Level.INFO, "nodes processed : " + nodesCounter + ", posts processed : " + postsCounter + ", posts with stack traces : " + interestingPostsCounter);
                }
                XMLEvent event = reader.nextEvent();
                nodesCounter++;
                if (filter.test(event)) {
                    Post post = new Post(event);
                    interestingPostsCounter++;
                    for (XMLEventProcessor processor : processors) {
                        try {
                            logger.log(Level.INFO, "processing post " + post.getId());
                            processor.process(event);
                        } catch (Exception processingException) {
                            logger.log(Level.WARNING, "error processing post " + post.getId() + " : " + processingException.getMessage(), processingException);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            xmlInputStream.close();
            for (XMLEventProcessor processor : processors) {
                processor.close();
            }

        }
        long ending = System.nanoTime();
        logger.log(Level.INFO, "Time spent : " + (ending - beginning) + " ns");
    }
}
