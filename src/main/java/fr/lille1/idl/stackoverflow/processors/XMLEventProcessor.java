package fr.lille1.idl.stackoverflow.processors;

import javax.xml.stream.events.XMLEvent;

/**
 * Created by dorian on 21/11/14.
 */
public interface XMLEventProcessor {
    public void process(final XMLEvent event) throws Exception;

    public void close() throws Exception;
}
