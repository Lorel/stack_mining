package fr.lille1.idl.stackoverflow.parsers.java;

import java.util.ArrayList;
import java.util.List;

import de.tud.stacktraces.evaluation.datastruct.StackTrace;
import de.tud.stacktraces.evaluation.datastruct.StackTraceParser;
import fr.lille1.idl.stackoverflow.parsers.StackTraceItf;
import fr.lille1.idl.stackoverflow.parsers.StackTrackParserItf;

public class JavaStackTraceParser implements StackTrackParserItf {

	public JavaStackTraceParser() {}

	@Override
	public List<StackTraceItf> parseText(String text) {
		List<StackTraceItf> returnList = new ArrayList<StackTraceItf>();
		for (StackTrace stackTrace : StackTraceParser.parseAll(text)) {
			returnList.add((StackTraceItf) new JavaStackTrace(stackTrace));
		}
		return returnList;
	}
}
