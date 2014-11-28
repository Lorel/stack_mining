package fr.lille1.idl.stackoverflow.filters;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.models.Post;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dorian on 25/11/14.
 */
public class JavaFilter implements XMLEventFilter {
    private Logger logger = Logger.getGlobal();

    @Override
    public boolean test(XMLEvent event) throws XMLStreamException {
        if (!event.isStartElement()) {
            return false;
        }
        StartElement start = event.asStartElement();
        QName startName = start.getName();
        if (!startName.toString().trim().equalsIgnoreCase("row")) {
            return false;
        }
        Attribute acceptedAnswer = start.getAttributeByName(new QName("AcceptedAnswerId"));
        Attribute parentId = start.getAttributeByName(new QName("parentId"));
        Attribute tagsAttribute = start.getAttributeByName(new QName("Tags"));
        String tags = tagsAttribute.toString().toLowerCase();
        if (acceptedAnswer == null || parentId != null || !tags.contains("java") || tags.contains("javascript")) {
            return false;
        }
        Post post = new Post(event);
        List<StackTrace> traces = null;
        try {
            traces = StackTraceParser.parseAll(post.getBody());
        } catch (StackOverflowError e) {
            logger.log(Level.SEVERE, "Crashed on post " + post.getId(), e);
            logger.log(Level.INFO, event.toString());
            return false;
        }
        return !traces.isEmpty();
    }
}
