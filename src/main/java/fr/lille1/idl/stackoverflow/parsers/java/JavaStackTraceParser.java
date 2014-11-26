package fr.lille1.idl.stackoverflow.parsers.java;

import java.util.ArrayList;
import java.util.List;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.StackTrackParserItf;

public class JavaStackTraceParser implements StackTrackParserItf {

	public JavaStackTraceParser() {}

	public List<StackTraceItf> parseText(String text) {
		List<StackTraceItf> returnList = new ArrayList<StackTraceItf>();
		for (StackTrace javaStackTrace : StackTraceParser.parseAll(text)) {
			returnList.add((StackTraceItf) javaStackTrace);
		}
		return returnList;
	}
}
