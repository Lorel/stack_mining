package fr.lille1.idl.stackoverflow.parsers;

import java.util.List;

public interface StackTrackParserItf {

	public List<StackTraceItf> parseText(String text);
}
