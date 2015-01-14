package fr.lille1.idl.stackoverflow.filters;

import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.java.JavaStackTraceParser;
import fr.lille1.idl.stackoverflow.tables.Post;

import javax.xml.namespace.QName;
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

    public boolean test(XMLEvent event) {
        if (!event.isStartElement()) {
            return false;
        }
        StartElement start = event.asStartElement();
        QName startName = start.getName();
        if (!startName.toString().trim().equalsIgnoreCase("row")) {
            return false;
        }
        Attribute acceptedAnswer = start.getAttributeByName(new QName("AcceptedAnswerId"));
        if (acceptedAnswer == null) {
            return false;
        }
        Attribute parentId = start.getAttributeByName(new QName("parentId"));
        if (parentId == null) {
            return false;
        }
        Attribute tagsAttribute = start.getAttributeByName(new QName("Tags"));
        if (tagsAttribute == null) {
            return false;
        }
        String tags = tagsAttribute.toString().toLowerCase();
        if (!tags.contains("java") || tags.contains("javascript")) {
            return false;
        }
        Post post = new Post(event);
        List<StackTraceItf> traces = null;
        try {
            traces = (new JavaStackTraceParser()).parseAll(post.getBody());
        } catch (StackOverflowError e) {
            logger.log(Level.SEVERE, "Crashed on post " + post.getId(), e);
            logger.log(Level.INFO, event.toString());
            return false;
        }
        return !traces.isEmpty();
    }
}
