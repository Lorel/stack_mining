package fr.lille1.idl.stackoverflow.filters;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by dorian on 25/11/14.
 */
public interface XMLEventFilter {
    public boolean test(final XMLEvent event) throws XMLStreamException;
}
