package fr.lille1.idl.stackoverflow.parsers;

import java.util.List;

public interface StackTraceParserItf {

	public List<StackTraceItf> parseAll(String text);
}
